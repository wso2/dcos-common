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

public class Docker {
    private String image;
    private String network;
    private boolean forcePullImage;
    private Collection<Port> portMappings;
    private List<Parameter> parameters;
    private boolean privileged;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public Collection<Port> getPortMappings() {
        return portMappings;
    }

    public void setPortMappings(Collection<Port> portMappings) {
        this.portMappings = portMappings;
    }

    public boolean isPrivileged() {
        return privileged;
    }

    public void setPrivileged(boolean privileged) {
        this.privileged = privileged;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public boolean isForcePullImage() {
        return forcePullImage;
    }

    public void setForcePullImage(boolean forcePullImage) {
        this.forcePullImage = forcePullImage;
    }

    @Override
    public String toString() {
        return ModelUtils.toString(this);
    }

}
