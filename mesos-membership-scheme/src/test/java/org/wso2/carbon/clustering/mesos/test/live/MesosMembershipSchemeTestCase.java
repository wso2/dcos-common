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
package org.wso2.carbon.clustering.mesos.test.live;

import com.hazelcast.config.Config;
import com.hazelcast.config.TcpIpConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.apache.axis2.clustering.ClusteringMessage;
import org.apache.axis2.description.Parameter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.annotations.Test;
import org.wso2.carbon.clustering.mesos.MesosConstants;
import org.wso2.carbon.clustering.mesos.MesosMembershipScheme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MesosMembershipSchemeTestCase {
    private static final Log log = LogFactory.getLog(MesosMembershipSchemeTestCase.class);
    private Config primaryHazelcastConfig;
    private HazelcastInstance primaryHazelcastInstance;
    private Map<String, Parameter> parameters;
    private List<ClusteringMessage> messageBuffer;

    @Test
    public void testInit() throws Exception {
        parameters = new HashMap<>();
        parameters.put(MesosConstants.MARATHON_ENDPOINT, new Parameter(MesosConstants.MARATHON_ENDPOINT,
                "http://mesos:8080"));
        parameters.put(MesosConstants.MARATHON_APP_ID, new Parameter(MesosConstants.MARATHON_ENDPOINT,
                "/product/service/wso2esb-worker"));
        parameters.put(MesosConstants.MARATHON_APPLICATIONS, new Parameter(MesosConstants.MARATHON_ENDPOINT,
                "/product/service/wso2esb-manager"));


        String primaryDomain = "TestDomain";
        messageBuffer = new ArrayList<>();
        primaryHazelcastConfig = new Config();
        primaryHazelcastInstance = Hazelcast.newHazelcastInstance(primaryHazelcastConfig);

        MesosMembershipScheme mesosMembershipScheme = new MesosMembershipScheme(parameters, primaryDomain,
                primaryHazelcastConfig, primaryHazelcastInstance, messageBuffer);
        mesosMembershipScheme.init();
        TcpIpConfig tcpIpConfig = primaryHazelcastConfig.getNetworkConfig().getJoin().getTcpIpConfig();
        log.info("Member list: " + tcpIpConfig.getMembers());
    }

}
