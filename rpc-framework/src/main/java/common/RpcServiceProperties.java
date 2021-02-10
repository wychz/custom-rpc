package common;

import lombok.Builder;

@Builder
public class RpcServiceProperties {
    private String version;
    private String group;
    private String serviceName;

    public RpcServiceProperties() {
    }

    public RpcServiceProperties(String version, String group, String serviceName) {
        this.version = version;
        this.group = group;
        this.serviceName = serviceName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public String toString() {
        return "RpcServiceProperties{" +
                "version='" + version + '\'' +
                ", group='" + group + '\'' +
                ", serviceName='" + serviceName + '\'' +
                '}';
    }

    public String toRpcServiceName() {
        return this.getServiceName() + this.getGroup() + this.getVersion();
    }
}
