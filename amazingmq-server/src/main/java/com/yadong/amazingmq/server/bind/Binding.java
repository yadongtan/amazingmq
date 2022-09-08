package com.yadong.amazingmq.server.bind;

/**
* @author YadongTan
* @date 2022/9/4 23:16
* @Description the super interface of Binds
*/
public interface Binding {

    public String getQueueName();

    public CommonBinding setQueueName(String queueName);

    public String getExchangeName();

    public CommonBinding setExchangeName(String exchangeName);

    public String getRoutingKey();

    public CommonBinding setRoutingKey(String routingKey);

}
