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
import org.wso2.carbon.clustering.mesos.client.model.v2.*;
import org.wso2.carbon.clustering.mesos.client.utils.MarathonException;

import java.util.List;

public interface Marathon {
    // Apps
    @RequestLine("GET /v2/apps")
    GetAppsResponse getApps() throws MarathonException;

    @RequestLine("GET /v2/apps/{id}")
    GetAppResponse getApp(@Param("id") String id) throws MarathonException;

    @RequestLine("GET /v2/apps/{id}?cmd={cmd}&embed={embed}")
    GetAppResponse getApp(@Param("id") String id, @Param("cmd") String cmd, @Param("embed") List<String> embed)
            throws MarathonException;

    @RequestLine("GET /v2/apps/{id}/tasks")
    GetAppTasksResponse getAppTasks(@Param("id") String id);

    @RequestLine("GET /v2/tasks")
    GetTasksResponse getTasks() throws MarathonException;

    @RequestLine("POST /v2/apps")
    App createApp(App app) throws MarathonException;

    @RequestLine("PUT /v2/apps/{app_id}")
    void updateApp(@Param("app_id") String appId, App app) throws MarathonException;

    @RequestLine("POST /v2/apps/{id}/restart?force={force}")
    void restartApp(@Param("id") String id, @Param("force") boolean force) throws MarathonException;

    @RequestLine("DELETE /v2/apps/{id}")
    Result deleteApp(@Param("id") String id) throws MarathonException;

    @RequestLine("DELETE /v2/apps/{app_id}/tasks?host={host}&scale={scale}")
    DeleteAppTasksResponse deleteAppTasks(@Param("app_id") String appId,
                                          @Param("host") String host, @Param("scale") String scale) throws
            MarathonException;

    @RequestLine("DELETE /v2/apps/{app_id}/tasks/{task_id}?scale={scale}")
    DeleteAppTaskResponse deleteAppTask(@Param("app_id") String appId,
                                        @Param("task_id") String taskId, @Param("scale") String scale) throws
            MarathonException;

    // Groups
    @RequestLine("POST /v2/groups")
    Result createGroup(Group group) throws MarathonException;

    @RequestLine("DELETE /v2/groups/{id}")
    Result deleteGroup(@Param("id") String id) throws MarathonException;

    @RequestLine("GET /v2/groups/{id}")
    Group getGroup(@Param("id") String id) throws MarathonException;

    // Tasks

    // Deployments
    @RequestLine("GET /v2/deployments")
    List<Deployment> getDeployments() throws MarathonException;

    @RequestLine("DELETE /v2/deployments/{deploymentId}")
    void cancelDeploymentAndRollback(@Param("deploymentId") String id) throws MarathonException;

    @RequestLine("DELETE /v2/deployments/{deploymentId}?force=true")
    void cancelDeployment(@Param("deploymentId") String id) throws MarathonException;

    // Event Subscriptions

    @RequestLine("POST /v2/eventSubscriptions?callbackUrl={url}")
    public GetEventSubscriptionRegisterResponse register(@Param("url") String url) throws MarathonException;

    @RequestLine("DELETE /v2/eventSubscriptions?callbackUrl={url}")
    public GetEventSubscriptionRegisterResponse unregister(@Param("url") String url) throws MarathonException;

    @RequestLine("GET /v2/eventSubscriptions")
    public GetEventSubscriptionsResponse subscriptions() throws MarathonException;

    // Queue
    @RequestLine("GET /v2/queue")
    QueueResponse getQueue() throws MarathonException;

    // Server Info
    @RequestLine("GET /v2/info")
    GetServerInfoResponse getServerInfo() throws MarathonException;

    // Miscellaneous


}