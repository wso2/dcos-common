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

import java.util.Collection;

public class Group {
    private String id;
    private Collection<App> apps;
    private Collection<Group> groups;
    private Collection<String> dependencies;
    private String version;

    public Group() {
    }

    public Group(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Collection<App> getApps() {
        return apps;
    }

    public void setApps(Collection<App> apps) {
        this.apps = apps;
    }

    public Collection<Group> getGroups() {
        return groups;
    }

    public void setGroups(Collection<Group> groups) {
        this.groups = groups;
    }

    public Collection<String> getDependencies() {
        return dependencies;
    }

    public void setDependencies(Collection<String> dependencies) {
        this.dependencies = dependencies;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return ModelUtils.toString(this);
    }
}
