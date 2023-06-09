package com.yadong.amazingmq.server.exchange;


import com.yadong.amazingmq.frame.Message;
import com.yadong.amazingmq.server.bind.Binding;
import com.yadong.amazingmq.server.queue.OutOfMaxLengthException;
import com.yadong.amazingmq.server.vhost.VirtualHost;

/**
* @author YadongTan
* @date 2022/9/4 23:15
* @Description the super interface of exchanges
*/
public interface Exchange {

    public VirtualHost getVhost();

    public void setVhost(VirtualHost vhost);

    public String getExchangeName();

    public void setExchangeName(String exchangeName);

    public String getExchangeType();

    public void setExchangeType(String exchangeType);

    public boolean isDuration();

    public void setDuration(boolean duration);

    public void setBinding(Binding binding);

    //不同的交换机重写这个方法来实现不同的路由
    public boolean sendMessageToQueue(String routingKey, Message message) throws OutOfMaxLengthException;

    public void removeQueueByRoutingKey(String routingKey);

    public void removeQueueByName(String queueName);

}
