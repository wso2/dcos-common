/*
* Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.wso2.carbon.clustering.mesos;

import com.hazelcast.config.Config;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.TcpIpConfig;
import com.hazelcast.core.*;
import org.apache.axis2.clustering.ClusteringFault;
import org.apache.axis2.clustering.ClusteringMessage;
import org.apache.axis2.description.Parameter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.clustering.mesos.client.Marathon;
import org.wso2.carbon.clustering.mesos.client.MarathonClient;
import org.wso2.carbon.clustering.mesos.client.model.v2.Task;
import org.wso2.carbon.clustering.mesos.client.utils.HttpStatus;
import org.wso2.carbon.clustering.mesos.client.utils.MarathonException;
import org.wso2.carbon.core.clustering.hazelcast.HazelcastCarbonClusterImpl;
import org.wso2.carbon.core.clustering.hazelcast.HazelcastMembershipScheme;
import org.wso2.carbon.core.clustering.hazelcast.HazelcastUtil;
import org.wso2.carbon.utils.xml.StringUtils;

import java.util.*;

import static org.wso2.carbon.clustering.mesos.MesosConstants.*;

/**
 * Mesos membership scheme provides cluster discovery for WSO2 Carbon servers on Apache Mesos platform
 */
public class MesosMembershipScheme implements HazelcastMembershipScheme {

    private static final Log log = LogFactory.getLog(MesosMembershipScheme.class);
    private final NetworkConfig nwConfig;
    private final Map<String, Parameter> parameters;
    private final List<ClusteringMessage> messageBuffer;
    private final String primaryDomain;
    private HazelcastInstance primaryHazelcastInstance;
    private HazelcastCarbonClusterImpl carbonCluster;
    private Marathon marathonClient;
    private List<String> marathonAppIdList;

    public MesosMembershipScheme(Map<String, Parameter> parameters,
                                 String primaryDomain,
                                 Config config,
                                 HazelcastInstance primaryHazelcastInstance,
                                 List<ClusteringMessage> messageBuffer) {
        this.parameters = parameters;
        this.primaryDomain = primaryDomain;
        this.primaryHazelcastInstance = primaryHazelcastInstance;
        this.messageBuffer = messageBuffer;
        this.nwConfig = config.getNetworkConfig();
    }

    @Override
    public void setPrimaryHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.primaryHazelcastInstance = hazelcastInstance;
    }

    @Override
    public void setLocalMember(Member localMember) {
    }

    @Override
    public void setCarbonCluster(HazelcastCarbonClusterImpl hazelcastCarbonCluster) {
        this.carbonCluster = hazelcastCarbonCluster;
    }

    @Override
    public void init() throws ClusteringFault {
        try {
            log.info("Initializing clustering membership scheme for Apache Mesos...");
            nwConfig.getJoin().getMulticastConfig().setEnabled(false);
            nwConfig.getJoin().getAwsConfig().setEnabled(false);
            TcpIpConfig tcpIpConfig = nwConfig.getJoin().getTcpIpConfig();
            tcpIpConfig.setEnabled(true);
            configureTcpIpParameters();
            loadConfiguration();

            for (String marathonAppId : marathonAppIdList) {
                if (StringUtils.isEmpty(marathonAppId)) {
                    continue;
                }
                if (log.isDebugEnabled()) {
                    log.debug(String.format("Retrieving Marathon application information for [appId] %s",
                            marathonAppId));
                }
                try {
                    Collection<Task> tasksCollection = marathonClient.getApp(marathonAppId).getApp().getTasks();
                    for (Task task : tasksCollection) {
                        addMembersInTask(task);
                    }
                } catch (MarathonException marathonEx) {
                    if (marathonEx.getStatus() == HttpStatus.SC_NOT_FOUND) {
                        /* Log a warning and continue, let dependent applications (that may not have been deployed yet)
                        add this node to the cluster */
                        log.warn(String.format("Marathon application [id] %s was not found", marathonAppId));
                    } else if (marathonEx.getStatus() == HttpStatus.SC_REQUEST_TIMEOUT) {
                        throw new ClusteringFault("Marathon REST API call timed out. Please check whether Marathon " +
                                "framework is accessible via the given endpoint");
                    } else {
                        throw new ClusteringFault(String.format("Failed to retrieve the Marathon application " +
                                "information: [id] %s", marathonAppId), marathonEx);
                    }
                }
            }
            log.info("Mesos clustering membership scheme initialized successfully");
        } catch (Exception e) {
            throw new ClusteringFault("Mesos clustering membership initialization failed", e);
        }
    }

    private void addMembersInTask(Task task) throws Exception {
        String hostname = task.getHost();
        if (StringUtils.isEmpty(hostname)) {
            throw new ClusteringFault(String.format("Hostname is empty for Marathon task [app] %s, " +
                    "[id] %s", task.getAppId(), task.getId()));
        }
        if (task.getPorts() == null || task.getPorts().size() == 0) {
            throw new ClusteringFault(String.format("Port list is empty for Marathon task [app] %s, " +
                    "[id] %s", task.getAppId(), task.getId()));
        }
        Integer port = task.getPorts().iterator().next();
        String memberStr = hostname + ":" + port.toString();
        nwConfig.getJoin().getTcpIpConfig().addMember(memberStr);
        log.info(String.format("Member added to cluster configuration: [member] %s", memberStr));
    }

    @Override
    public void joinGroup() throws ClusteringFault {
        primaryHazelcastInstance.getCluster().addMembershipListener(new MesosMembershipSchemeListener());
    }

    private void loadConfiguration() {
        String marathonEndpoint = getParameterValue(MARATHON_ENDPOINT, DEFAULT_MARATHON_ENDPOINT);
        String marathonAppIds = getParameterValue(MARATHON_APPLICATIONS, "");
        boolean enableBasicAuth = Boolean.parseBoolean(getParameterValue(ENABLE_BASIC_AUTH, "false"));
        String marathonUsername = getParameterValue(MARATHON_USERNAME, "");
        String marathonPassword = getParameterValue(MARATHON_PASSWORD, "");
        String localAppId = getParameterValue(MARATHON_APP_ID, "");

        log.info(String.format("Mesos clustering membership scheme configuration: [endpoint] %s, " +
                        "[app-ids] %s, [auth] %s, [local-app-id] %s", marathonEndpoint, marathonAppIds,
                enableBasicAuth, localAppId));

        if (enableBasicAuth) {
            marathonClient = MarathonClient.getInstanceWithBasicAuth(marathonEndpoint, marathonUsername,
                    marathonPassword);
        } else {
            marathonClient = MarathonClient.getInstance(marathonEndpoint);
        }

        marathonAppIdList = new ArrayList<>(Arrays.asList(marathonAppIds.split(",")));
        if (StringUtils.isEmpty(localAppId)) {
            throw new IllegalArgumentException("MARATHON_APP_ID property was not found in environment variables");
        }

        if (!marathonAppIdList.contains(localAppId)) {
            marathonAppIdList.add(localAppId);
        }
        log.info("Marathon application list: " + marathonAppIdList);
    }

    private String getParameterValue(String name, String def) {
        String value = System.getenv(name);
        if (StringUtils.isEmpty(value)) {
            Parameter parameter = parameters.get(name);
            if (parameter == null || StringUtils.isEmpty((String) parameter.getValue())) {
                if (def == null) {
                    throw new IllegalArgumentException(String.format("Clustering parameter [name] %s not found", name));
                } else {
                    value = def;
                }
            } else {
                value = (String) parameter.getValue();
            }
        }
        return value;
    }

    private void configureTcpIpParameters() throws ClusteringFault {
        Parameter connTimeout = parameters.get(MesosConstants.CONNECTION_TIMEOUT);
        TcpIpConfig tcpIpConfig = nwConfig.getJoin().getTcpIpConfig();
        if (connTimeout != null && connTimeout.getValue() != null) {
            tcpIpConfig.
                    setConnectionTimeoutSeconds(Integer.parseInt(((String) (connTimeout.getValue())).trim()));
        }
    }

    /**
     * Apache Mesos clustering membership scheme listener
     */
    private class MesosMembershipSchemeListener implements MembershipListener {
        private final Log log = LogFactory.getLog(MesosMembershipSchemeListener.class);

        @Override
        public void memberAdded(MembershipEvent membershipEvent) {
            Member member = membershipEvent.getMember();

            // Send all cluster messages
            carbonCluster.memberAdded(member);
            log.info(String.format("Member joined: [UUID] %s, [Address] %s", member.getUuid(),
                    member.getSocketAddress().toString()));
            // Wait for sometime for the member to completely join before replaying messages
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ignored) {
            }
            HazelcastUtil.sendMessagesToMember(messageBuffer, member, carbonCluster);
        }

        @Override
        public void memberRemoved(MembershipEvent membershipEvent) {
            Member member = membershipEvent.getMember();
            carbonCluster.memberRemoved(member);
            log.info(String.format("Member left: [UUID] %s, [Address] %s", member.getUuid(),
                    member.getSocketAddress().toString()));
        }

        @Override
        public void memberAttributeChanged(MemberAttributeEvent memberAttributeEvent) {
            if (log.isDebugEnabled()) {
                log.debug(String.format("Member attribute changed: [Key] %s, [Value] %s", memberAttributeEvent.getKey(),
                        memberAttributeEvent.getValue()));
            }
        }
    }
}
