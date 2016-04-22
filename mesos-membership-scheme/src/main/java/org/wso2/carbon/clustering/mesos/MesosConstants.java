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

public class MesosConstants {
    public static final String CONNECTION_TIMEOUT = "connTimeout";
    public static final String MARATHON_ENDPOINT = "MARATHON_ENDPOINT";
    public static final String MARATHON_APPLICATIONS = "MARATHON_APPLICATIONS";
    public static final String MARATHON_USERNAME = "MARATHON_USERNAME";
    public static final String MARATHON_PASSWORD = "MARATHON_PASSWORD";
    public static final String ENABLE_BASIC_AUTH = "ENABLE_BASIC_AUTH";
    public static final String MARATHON_APP_ID = "MARATHON_APP_ID";
    public static final String MARATHON_HOST = "HOST";
    public static final String DEFAULT_MARATHON_ENDPOINT = "http://" + System.getenv(MARATHON_HOST) + ":8080";
    public static final String APPLICATION_JSON = "application/json";
}
