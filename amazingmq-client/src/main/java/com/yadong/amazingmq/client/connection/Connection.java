package com.yadong.amazingmq.client.connection;

import com.yadong.amazingmq.client.channel.AMQChannel;
import com.yadong.amazingmq.client.channel.Channel;
import com.yadong.amazingmq.client.frame.FrameFactory;
import com.yadong.amazingmq.client.netty.AmazingMqNettyClient;
import com.yadong.amazingmq.client.netty.handler.BrokerNettyClient;
import com.yadong.amazingmq.frame.Frame;
import com.yadong.amazingmq.payload.ConnectionCreatedPayload;
import com.yadong.amazingmq.utils.ObjectMapperUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

public class Connection {

    private final AtomicInteger channelIdGenerator = new AtomicInteger(1);
    private final AtomicInteger connectionIdGenerator = new AtomicInteger(1);
    private static final Logger logger = LoggerFactory.getLogger(Connection.class);

    private String username;
    private String password;
    private String host;
    private int port;
    private String virtualHost;
    private BrokerNettyClient client;
    // private List<Channel> channelList; 这里绝不能用channelList, 会造成内存泄露
    private HashMap<Short, Channel> channelMap;
    private short connectionId;

    public Connection(){
        this.connectionId = (short) connectionIdGenerator.getAndIncrement();
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
        Frame result = client.syncSend(null, connectionFrame);
        if(result != null){
            if(result.getType() == Frame.PayloadType.CREATE_CONNECTION_SUCCESS.getType()){
                this.connectionId = ObjectMapperUtils.toObject(result.getPayload(), ConnectionCreatedPayload.class).getConnectionId();
                logger.info("创建Connection成功");
            }else{
                logger.info("创建Connection失败");
            }
        }
        return this;
    }

    public void reconnectClient() throws InterruptedException, ExecutionException {
        client = AmazingMqNettyClient.createAndConnect(host, port);
        Frame connectionFrame = FrameFactory.createConnectionFrame(this);
        Frame result = client.syncSend(null, connectionFrame);
        if(result != null){
            if(result.getType() == Frame.PayloadType.CREATE_CONNECTION_SUCCESS.getType()){
                this.connectionId = ObjectMapperUtils.toObject(result.getPayload(), ConnectionCreatedPayload.class).getConnectionId();
                logger.info("切换Connection成功");
            }else{
                logger.info("切换Connection失败");
            }
        }
    }

    public short getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(short connectionId) {
        this.connectionId = connectionId;
    }


    public Channel createChannel() throws ExecutionException, InterruptedException {
        AMQChannel channel = new AMQChannel(channelIdGenerator.getAndIncrement(), this, client);

        Frame result = client.syncSend(channel, FrameFactory.createChannelFrame(channel));
        if(result != null){
            if(result.getType() == Frame.PayloadType.SUCCESSFUL.getType()){
                logger.info("创建Channel成功");
            }else{
                logger.info("创建Channel失败");
            }
        }
        return channel;
    }


}
