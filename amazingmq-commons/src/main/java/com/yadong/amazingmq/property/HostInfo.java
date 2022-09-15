package com.yadong.amazingmq.property;


import java.util.Objects;

public class HostInfo {
    private String ip;
    private int port;

    public HostInfo(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HostInfo hostInfo = (HostInfo) o;
        return port == hostInfo.port && Objects.equals(ip, hostInfo.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port);
    }
}
