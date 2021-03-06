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
import org.wso2.carbon.clustering.mesos.client.model.dns.v1.MesosDNSSRVRecord;
import org.wso2.carbon.clustering.mesos.client.utils.MesosException;

import java.util.List;

public interface MesosDNS {

    @RequestLine("GET /v1/services/{service}")
    List<MesosDNSSRVRecord> getService(@Param("service") String service) throws MesosException;
}
