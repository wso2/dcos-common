package org.wso2.carbon.clustering.mesos.client.model.marathon.v2;

public class IpAddress {

    private String ipAddress;

    private String protocol;

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

}
