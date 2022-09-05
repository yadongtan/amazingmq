package com.yadong.amazingmq.client.connection;

import com.yadong.amazingmq.client.channel.Channel;
import com.yadong.amazingmq.client.frame.FrameFactory;
import com.yadong.amazingmq.client.netty.AmazingMqNettyClient;
import com.yadong.amazingmq.client.netty.handler.BrokerNettyClient;
import com.yadong.amazingmq.frame.Frame;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class Connection {

    private String username;
    private String password;
    private String host;
    private int port;
    private String virtualHost;
    private BrokerNettyClient client;
    // private List<Channel> channelList; 这里绝不能用channelList, 会造成内存泄露

    public Connection(){

    }

    public BrokerNettyClient getClient() {
        return client;
    }

    public void setClient(BrokerNettyClient client) {
        this.client = client;
    }

    public String getUsername() {
        return username;
    }

    public Connection setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Connection setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getHost() {
        return host;
    }

    public Connection setHost(String host) {
        this.host = host;
        return this;
    }

    public String getVirtualHost() {
        return virtualHost;
    }

    public Connection setVirtualHost(String virtualHost) {
        this.virtualHost = virtualHost;
        return this;
    }

    public int getPort() {
        return port;
    }

    public Connection setPort(int port) {
        this.port = port;
        return this;
    }

    public Connection doConnect() throws InterruptedException, ExecutionException {
        client = AmazingMqNettyClient.createAndConnect(host, port);
        // 发送一个Connection帧,表示与服务端想建立一个Connection
        Frame connectionFrame = FrameFactory.createConnectionFrame(this);
        Frame result = client.syncSend(connectionFrame);
        if(result != null){
            // TODO: 2022/9/5 判断一下发起Connection请求后返回的帧是否是成功的 !
        }
        return this;
    }
}
