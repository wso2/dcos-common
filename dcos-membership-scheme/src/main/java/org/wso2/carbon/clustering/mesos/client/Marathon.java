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
package org.wso2.carbon.clustering.mesos.client;

import feign.Param;
import feign.RequestLine;
import org.wso2.carbon.clustering.mesos.client.model.marathon.v2.*;
import org.wso2.carbon.clustering.mesos.client.utils.MesosException;

import java.util.List;

public interface Marathon {
    // Apps
    @RequestLine("GET /v2/apps")
    GetAppsResponse getApps() throws MesosException;

    @RequestLine("GET /v2/apps/{id}")
    GetAppResponse getApp(@Param("id") String id) throws MesosException;

    @RequestLine("GET /v2/apps/{id}?cmd={cmd}&embed={embed}")
    GetAppResponse getApp(@Param("id") String id, @Param("cmd") String cmd, @Param("embed") List<String> embed)
            throws MesosException;

    @RequestLine("GET /v2/apps/{id}/tasks")
    GetAppTasksResponse getAppTasks(@Param("id") String id);

    @RequestLine("GET /v2/tasks")
    GetTasksResponse getTasks() throws MesosException;

    @RequestLine("POST /v2/apps")
    App createApp(App app) throws MesosException;

    @RequestLine("PUT /v2/apps/{app_id}")
    void updateApp(@Param("app_id") String appId, App app) throws MesosException;

    @RequestLine("POST /v2/apps/{id}/restart?force={force}")
    void restartApp(@Param("id") String id, @Param("force") boolean force) throws MesosException;

    @RequestLine("DELETE /v2/apps/{id}")
    Result deleteApp(@Param("id") String id) throws MesosException;

    @RequestLine("DELETE /v2/apps/{app_id}/tasks?hostname={hostname}&scale={scale}")
    DeleteAppTasksResponse deleteAppTasks(@Param("app_id") String appId,
                                          @Param("hostname") String host, @Param("scale") String scale) throws
            MesosException;

    @RequestLine("DELETE /v2/apps/{app_id}/tasks/{task_id}?scale={scale}")
    DeleteAppTaskResponse deleteAppTask(@Param("app_id") String appId,
                                        @Param("task_id") String taskId, @Param("scale") String scale) throws
            MesosException;

    // Groups
    @RequestLine("POST /v2/groups")
    Result createGroup(Group group) throws MesosException;

    @RequestLine("DELETE /v2/groups/{id}")
    Result deleteGroup(@Param("id") String id) throws MesosException;

    @RequestLine("GET /v2/groups/{id}")
    Group getGroup(@Param("id") String id) throws MesosException;

    // Tasks

    // Deployments
    @RequestLine("GET /v2/deployments")
    List<Deployment> getDeployments() throws MesosException;

    @RequestLine("DELETE /v2/deployments/{deploymentId}")
    void cancelDeploymentAndRollback(@Param("deploymentId") String id) throws MesosException;

    @RequestLine("DELETE /v2/deployments/{deploymentId}?force=true")
    void cancelDeployment(@Param("deploymentId") String id) throws MesosException;

    // Event Subscriptions

    @RequestLine("POST /v2/eventSubscriptions?callbackUrl={url}")
    GetEventSubscriptionRegisterResponse register(@Param("url") String url) throws MesosException;

    @RequestLine("DELETE /v2/eventSubscriptions?callbackUrl={url}")
    GetEventSubscriptionRegisterResponse unregister(@Param("url") String url) throws MesosException;

    @RequestLine("GET /v2/eventSubscriptions")
    GetEventSubscriptionsResponse subscriptions() throws MesosException;

    // Queue
    @RequestLine("GET /v2/queue")
    QueueResponse getQueue() throws MesosException;

    // Server Info
    @RequestLine("GET /v2/info")
    GetServerInfoResponse getServerInfo() throws MesosException;
}