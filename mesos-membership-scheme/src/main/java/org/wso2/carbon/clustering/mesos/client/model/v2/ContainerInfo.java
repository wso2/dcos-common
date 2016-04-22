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

import java.util.ArrayList;
import java.util.Collection;


public class ContainerInfo {
    private String image;
    private Collection<String> options;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Collection<String> getOptions() {
        return options;
    }

    public void setOptions(Collection<String> options) {
        this.options = options;
    }

    public void addOption(String option) {
        if (options == null) {
            options = new ArrayList<String>();
        }
        options.add(option);
    }

    @Override
    public String toString() {
        return ModelUtils.toString(this);
    }

}
