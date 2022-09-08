package com.yadong.amazingmq.payload;


import java.util.concurrent.atomic.AtomicInteger;

/**
* @author YadongTan
* @date 2022/9/5 17:32
* @Description 当需要创建一个连接Connection时, 应该使用这个类放在payload中
*/
public class ConnectionPayload implements Payload{

    private short connectionId;
    private String username;
    private String password;
    private String vhost;

    public String getUsername() {
        return username;
    }

    public ConnectionPayload setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public ConnectionPayload setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getVhost() {
        return vhost;
    }

    public ConnectionPayload setVhost(String vhost) {
        this.vhost = vhost;
        return this;
    }

    public short getConnectionId() {
        return connectionId;
    }


    public void setConnectionId(short connectionId) {
        this.connectionId = connectionId;
    }

    public ConnectionPayload(){
    }



}
