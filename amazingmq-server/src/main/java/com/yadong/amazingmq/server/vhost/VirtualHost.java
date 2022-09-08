package com.yadong.amazingmq.server.vhost;


import com.yadong.amazingmq.server.bind.Binding;
import com.yadong.amazingmq.server.connection.Connection;
import com.yadong.amazingmq.server.exchange.Exchange;
import com.yadong.amazingmq.server.queue.Queue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
* @author YadongTan
* @date 2022/9/6 10:52
* @Description 管理自己的Connection
*/
public class VirtualHost {

    private String path;
    // 连接id 和 Connection
    private ConcurrentHashMap<Short, Connection> connectionMap
            = new ConcurrentHashMap<>();

    // 交换机name - exchange
    private ConcurrentHashMap<String, Exchange> exchangeMap;

    // 队列name - exchange
    private ConcurrentHashMap<String, Queue> queueMap;

    // 绑定name - Binding
    private ConcurrentHashMap<String, Binding> bindingMap;

    {
        // 初始化交换机
        exchangeMap = new ConcurrentHashMap<>(16);

        // 初始化队列
        queueMap = new ConcurrentHashMap<>(16);

        // 初始化绑定
        bindingMap = new ConcurrentHashMap<>(16);
    }

    public VirtualHost(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void addConnection(Connection connection){
        connectionMap.put(connection.getConnectionId(), connection);
    }

    public void removeConnection(short cid){
        connectionMap.remove(cid);
    }

    public ConcurrentHashMap<Short, Connection> getConnectionMap() {
        return connectionMap;
    }

    public void setConnectionMap(ConcurrentHashMap<Short, Connection> connectionMap) {
        this.connectionMap = connectionMap;
    }

    public ConcurrentHashMap<String, Exchange> getExchangeMap() {
        return exchangeMap;
    }

    public void setExchangeMap(ConcurrentHashMap<String, Exchange> exchangeMap) {
        this.exchangeMap = exchangeMap;
    }

    public ConcurrentHashMap<String, Queue> getQueueMap() {
        return queueMap;
    }

    public void setQueueMap(ConcurrentHashMap<String, Queue> queueMap) {
        this.queueMap = queueMap;
    }

    public ConcurrentHashMap<String, Binding> getBindingMap() {
        return bindingMap;
    }

    public void setBindingMap(ConcurrentHashMap<String, Binding> bindingMap) {
        this.bindingMap = bindingMap;
    }

    public void addExchange(Exchange exchange){
        exchangeMap.put(exchange.getExchangeName(), exchange);
    }

    public Exchange getExchange(String exchangeName){
        return exchangeMap.get(exchangeName);
    }
}
