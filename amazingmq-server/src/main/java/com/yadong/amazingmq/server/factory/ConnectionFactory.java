package com.yadong.amazingmq.server.factory;

import com.yadong.amazingmq.frame.Frame;
import com.yadong.amazingmq.payload.ConnectionPayload;
import com.yadong.amazingmq.payload.Payload;
import com.yadong.amazingmq.server.AmazingMqBroker;
import com.yadong.amazingmq.server.connection.CommonConnection;
import com.yadong.amazingmq.server.connection.Connection;
import com.yadong.amazingmq.server.netty.handler.BrokerNettyHandler;
import com.yadong.amazingmq.server.property.UserProperties;
import com.yadong.amazingmq.server.vhost.Auth;
import com.yadong.amazingmq.server.vhost.AuthErrorException;
import com.yadong.amazingmq.utils.ObjectMapperUtils;

public class ConnectionFactory extends AbstractBrokerFactory {

    private final ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<>();


    ConnectionFactory(Frame frame) {
        super(frame);
    }

    public Connection create(BrokerNettyHandler client) throws AuthErrorException {
        String payload = frame.getPayload();
        ConnectionPayload connectionPayload = ObjectMapperUtils.toObject(payload, ConnectionPayload.class);
        // short connectionId = connectionPayload.getConnectionId();
        // 这里的connectionId应该由Broker生成
        String username = connectionPayload.getUsername();
        String password = connectionPayload.getPassword();
        String vhost = connectionPayload.getVhost();
        UserProperties userProperties = new UserProperties(username, password, vhost);
        // 鉴权
        Auth.accessible(userProperties);
        // 创建连接
        Connection connection = new CommonConnection(AmazingMqBroker.getInstance().getVirtualHostMap().get(vhost));
        // 添加到Broker
        AmazingMqBroker.getInstance().getVirtualHostMap().get(vhost).addConnection(connection);
        // 添加到当前线程
        connectionThreadLocal.set(connection);
        return connection;
    }

    public Connection create(String virtualHost) throws AuthErrorException {
        String payload = frame.getPayload();
        ConnectionPayload connectionPayload = ObjectMapperUtils.toObject(payload, ConnectionPayload.class);
        // short connectionId = connectionPayload.getConnectionId();
        // 这里的connectionId应该由Broker生成
        String username = connectionPayload.getUsername();
        String password = connectionPayload.getPassword();
        String vhost = connectionPayload.getVhost();
        UserProperties userProperties = new UserProperties(username, password, vhost);
        // 鉴权
        Auth.accessible(userProperties);
        // 创建连接
        Connection connection = new CommonConnection(AmazingMqBroker.getInstance().getVirtualHostMap().get(vhost));
        // 添加到Broker
        AmazingMqBroker.getInstance().getVirtualHostMap().get(vhost).addConnection(connection);
        // 添加到当前线程
        connectionThreadLocal.set(connection);
        return connection;
    }

    @Override
    public Object create(String virtualHost, Payload payload) throws AuthErrorException {
        ConnectionPayload connectionPayload = (ConnectionPayload) payload;
        // short connectionId = connectionPayload.getConnectionId();
        // 这里的connectionId应该由Broker生成
        String username = connectionPayload.getUsername();
        String password = connectionPayload.getPassword();
        String vhost = connectionPayload.getVhost();
        UserProperties userProperties = new UserProperties(username, password, vhost);
        // 鉴权
        Auth.accessible(userProperties);
        // 创建连接
        Connection connection = new CommonConnection(AmazingMqBroker.getInstance().getVirtualHostMap().get(vhost));
        // 添加到Broker
        AmazingMqBroker.getInstance().getVirtualHostMap().get(vhost).addConnection(connection);
        // 添加到当前线程
        connectionThreadLocal.set(connection);
        return connection;
    }

}
