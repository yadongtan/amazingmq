package com.yadong.amazingmq.server.vhost;


import com.yadong.amazingmq.server.bind.Binding;
import com.yadong.amazingmq.server.connection.Connection;
import com.yadong.amazingmq.server.exchange.Exchange;
import com.yadong.amazingmq.server.queue.AmazingMqQueue;

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

    // 队列name - queue
    private ConcurrentHashMap<String, AmazingMqQueue> queueMap;

    // 绑定routingKey - Binding
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

    public void removeAndCloseConnection(short cid) {
        Connection remove = connectionMap.remove(cid);
        if(remove != null){
            // 遍历此连接的所有channel， 调用它们的关闭方法
            remove.getChannelMap().forEach((channelId, channel)->{
                channel.close();
            });
        }

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

    public ConcurrentHashMap<String, AmazingMqQueue> getQueueMap() {
        return queueMap;
    }

    public void setQueueMap(ConcurrentHashMap<String, AmazingMqQueue> queueMap) {
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

    public void addQueue(AmazingMqQueue queue){
        queueMap.put(queue.getQueueName(), queue);
    }

    public AmazingMqQueue getQueue(String queueName){
        return queueMap.get(queueName);
    }

    public void removeQueue(String queueName){
        queueMap.remove(queueName);
        exchangeMap.forEach((exchangeName, exchange)->{
            exchange.removeQueueByName(queueName);
        });
    }
}
