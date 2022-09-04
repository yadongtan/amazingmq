package com.yadong.amazingmq.server.property;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author YadongTan
 * @date 2022/9/4 21:49
 * @Description Broker的一些配置信息
 */
public class ServerProperties {

    // Broker启动的ip
    private String host;
    // Broker启动的端口
    private int port;

    public ServerProperties(String host, int port){
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
