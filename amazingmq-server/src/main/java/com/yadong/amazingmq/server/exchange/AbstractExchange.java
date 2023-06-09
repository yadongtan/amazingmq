package com.yadong.amazingmq.server.exchange;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yadong.amazingmq.server.bind.Binding;
import com.yadong.amazingmq.server.queue.AmazingMqQueue;
import com.yadong.amazingmq.server.vhost.VirtualHost;

import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractExchange implements Exchange{

    @JsonIgnore
    protected VirtualHost vhost;

    @JsonIgnore
    // 这里是交换机绑定的队列!
    // BindingKey - Queue
    protected ConcurrentHashMap<String, AmazingMqQueue> queueMap = new ConcurrentHashMap<>();

    protected String exchangeName;
    protected String exchangeType;
    protected boolean duration;

    public AbstractExchange(String exchangeName, String exchangeType, boolean duration) {
        this.exchangeName = exchangeName;
        this.exchangeType = exchangeType;
        this.duration = duration;
    }

    public void removeQueueByRoutingKey(String routingKey){
        queueMap.remove(routingKey);
    }

    public void removeQueueByName(String queueName){
        queueMap.forEach((routingKey, queue) -> {
            if(queue.getQueueName().equals(queueName)){
                queueMap.remove(routingKey);
            }
        });
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getExchangeType() {
        return exchangeType;
    }

    public void setExchangeType(String exchangeType) {
        this.exchangeType = exchangeType;
    }

    public boolean isDuration() {
        return duration;
    }

    public void setDuration(boolean duration) {
        this.duration = duration;
    }

    public VirtualHost getVhost() {
        return vhost;
    }

    public void setVhost(VirtualHost vhost) {
        this.vhost = vhost;
    }

    public void setBinding(Binding binding) {
        AmazingMqQueue queue = vhost.getQueue(binding.getQueueName());
        queueMap.put(binding.getRoutingKey(), queue);
    }
}
