package com.yadong.amazingmq.payload;


/**
* @author YadongTan
* @date 2022/9/5 17:32
* @Description 当需要创建一个连接Connection时, 应该使用这个类放在payload中
*/
public class ConnectionPayload implements Payload{

    private String fromHost;
    private String fromPort;
    private String destHost;
    private String destPort;

    public ConnectionPayload(){

    }

    public ConnectionPayload(String fromHost, String fromPort, String destHost, String destPort) {
        this.fromHost = fromHost;
        this.fromPort = fromPort;
        this.destHost = destHost;
        this.destPort = destPort;
    }

    public String getFromHost() {
        return fromHost;
    }

    public ConnectionPayload setFromHost(String fromHost) {
        this.fromHost = fromHost;
        return this;
    }

    public String getFromPort() {
        return fromPort;
    }

    public ConnectionPayload setFromPort(String fromPort) {
        this.fromPort = fromPort;
        return this;
    }

    public String getDestHost() {
        return destHost;
    }

    public ConnectionPayload setDestHost(String destHost) {
        this.destHost = destHost;
        return this;
    }

    public String getDestPort() {
        return destPort;
    }

    public ConnectionPayload setDestPort(String destPort) {
        this.destPort = destPort;
        return this;
    }

    @Override
    public String toString() {
        return "ConnectionFrame{" +
                "fromHost='" + fromHost + '\'' +
                ", fromPort='" + fromPort + '\'' +
                ", destHost='" + destHost + '\'' +
                ", destPort='" + destPort + '\'' +
                '}';
    }
}
