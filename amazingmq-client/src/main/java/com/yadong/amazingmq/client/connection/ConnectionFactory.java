package com.yadong.amazingmq.client.connection;

import com.yadong.amazingmq.property.CommonProperty;

import java.util.concurrent.ExecutionException;

public class ConnectionFactory {

    private String username;
    private String password;
    private String host;
    private int port = CommonProperty.DEFAULT_BROKER_PORT;  //如果不设置,就使用默认值
    private String virtualHost;

    public ConnectionFactory(){}

    public final Connection newConnection() throws InterruptedException, ExecutionException {
        Connection connection = new Connection()
                .setUsername(username)
                .setPassword(password)
                .setHost(host)
                .setPort(port)
                .setVirtualHost(virtualHost)
                .doConnect();

        return connection;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getVirtualHost() {
        return virtualHost;
    }

    public void setVirtualHost(String virtualHost) {
        this.virtualHost = virtualHost;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
