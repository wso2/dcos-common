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
package org.wso2.carbon.clustering.mesos.client.model.marathon.v2;

import org.wso2.carbon.clustering.mesos.client.utils.ModelUtils;

public class HealthCheckResult {

    private boolean alive;
    private int consecutiveFailures;
    private String firstSuccess;
    private String lastFailure;
    private String lastSuccess;
    private String taskId;

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public int getConsecutiveFailures() {
        return consecutiveFailures;
    }

    public void setConsecutiveFailures(int consecutiveFailures) {
        this.consecutiveFailures = consecutiveFailures;
    }

    public String getFirstSuccess() {
        return firstSuccess;
    }

    public void setFirstSuccess(String firstSuccess) {
        this.firstSuccess = firstSuccess;
    }

    public String getLastFailure() {
        return lastFailure;
    }

    public void setLastFailure(String lastFailure) {
        this.lastFailure = lastFailure;
    }

    public String getLastSuccess() {
        return lastSuccess;
    }

    public void setLastSuccess(String lastSuccess) {
        this.lastSuccess = lastSuccess;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return ModelUtils.toString(this);
    }
}
