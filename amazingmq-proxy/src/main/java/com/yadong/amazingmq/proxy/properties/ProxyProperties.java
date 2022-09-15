package com.yadong.amazingmq.proxy.properties;

import com.yadong.amazingmq.property.HostInfo;

import java.util.LinkedList;
import java.util.List;

public class ProxyProperties {

    private HostInfo proxyStartHostInfo;
    private final List<HostInfo> hostList = new LinkedList<>();

    public ProxyProperties(String ip, int port){
        proxyStartHostInfo = new HostInfo(ip, port);
    }

    public List<HostInfo> getHostList() {
        return hostList;
    }

    public void addHost(String ip, int port){
        hostList.add(new HostInfo(ip, port));
    }

    public void addHost(HostInfo hostInfo) {
        hostList.add(hostInfo);
    }

    public void removeHost(HostInfo hostInfo) {
        hostList.remove(hostInfo);
    }

    public HostInfo getProxyStartHostInfo() {
        return proxyStartHostInfo;
    }

    public void setProxyStartHostInfo(HostInfo proxyStartHostInfo) {
        this.proxyStartHostInfo = proxyStartHostInfo;
    }
}
