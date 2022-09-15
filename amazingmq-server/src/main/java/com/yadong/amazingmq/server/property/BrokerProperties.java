package com.yadong.amazingmq.server.property;


import com.yadong.amazingmq.property.HostInfo;

public class BrokerProperties {
    private HostInfo brokerHostInfo;
    private HostInfo clusterHostInfo;   //集群通讯开放的ip和端口

    public BrokerProperties(HostInfo brokerHostInfo) {
        this.brokerHostInfo = brokerHostInfo;
    }


    public BrokerProperties(HostInfo brokerHostInfo, HostInfo clusterHostInfo) {
        this.brokerHostInfo = brokerHostInfo;
        this.clusterHostInfo = clusterHostInfo;
    }

    public HostInfo getBrokerHostInfo() {
        return brokerHostInfo;
    }

    public void setBrokerHostInfo(HostInfo brokerHostInfo) {
        this.brokerHostInfo = brokerHostInfo;
    }

    public HostInfo getClusterHostInfo() {
        return clusterHostInfo;
    }

    public void setClusterHostInfo(HostInfo clusterHostInfo) {
        this.clusterHostInfo = clusterHostInfo;
    }
}
