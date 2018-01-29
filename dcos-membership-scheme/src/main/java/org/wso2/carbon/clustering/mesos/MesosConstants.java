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
    public static final String ENABLE_BASIC_AUTH = "ENABLE_MARATHON_BASIC_AUTH";
    public static final String IS_OVERLAY_NETWORK_AND_DOCKER = "IS_OVERLAY_NETWORK_AND_DOCKER";
    public static final String MARATHON_APP_ID = "MARATHON_APP_ID";
    public static final String MESOS_DNS_ENDPOINT = "MESOS_DNS_ENDPOINT";
    public static final String MESOS_MEMBER_DISCOVERY_SCHEME = "MESOS_MEMBER_DISCOVERY_SCHEME";
    public static final String MESOS_DNS_DISCOVERY_SCHEME = "MesosDNS";
    public static final String MESOS_MARATHON_DISCOVERY_SCHEME = "Marathon";
    public static final String DEFAULT_MARATHON_ENDPOINT = "http://marathon.mesos:8080";
    public static final String DEFAULT_MESOS_DNS_ENDPOINT = "http://marathon.mesos:8123";
    public static final String DNS_UPDATE_TIMEOUT = "DNS_UPDATE_TIMEOUT";
    public static final String IP_ADDRESS_PROTOCOL_IPV4 = "IPv4";
    public static final String DEFAULT_DNS_UPDATE_TIMEOUT = "10"; // in seconds
    public static final int DNS_RETRY_INTERVAL = 5; // in seconds
}
