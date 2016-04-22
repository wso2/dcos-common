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
package org.wso2.carbon.clustering.mesos.client.model.v2;

import org.wso2.carbon.clustering.mesos.client.utils.ModelUtils;

import java.util.Collection;
import java.util.List;

public class Deployment {
    private Collection<String> affectedApps;
    private String id;
    private List<List<Action>> steps;
    private Collection<Action> currentActions;
    private String version;
    private Integer currentStep;
    private Integer totalSteps;

    public Collection<String> getAffectedApps() {
        return affectedApps;
    }

    public void setAffectedApps(Collection<String> affectedApps) {
        this.affectedApps = affectedApps;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<List<Action>> getSteps() {
        return steps;
    }

    public void setSteps(List<List<Action>> steps) {
        this.steps = steps;
    }

    public Collection<Action> getCurrentActions() {
        return currentActions;
    }

    public void setCurrentActions(Collection<Action> currentActions) {
        this.currentActions = currentActions;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(Integer currentStep) {
        this.currentStep = currentStep;
    }

    public Integer getTotalSteps() {
        return totalSteps;
    }

    public void setTotalSteps(Integer totalSteps) {
        this.totalSteps = totalSteps;
    }

    @Override
    public String toString() {
        return ModelUtils.toString(this);
    }

    public class Action {
        private String action;
        private String app;

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getApp() {
            return app;
        }

        public void setApp(String app) {
            this.app = app;
        }

        @Override
        public String toString() {
            return ModelUtils.toString(this);
        }
    }
}
