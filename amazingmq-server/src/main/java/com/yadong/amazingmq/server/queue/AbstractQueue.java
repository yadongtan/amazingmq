package com.yadong.amazingmq.server.queue;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yadong.amazingmq.server.vhost.VirtualHost;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractQueue implements Queue{

    @JsonIgnore
    protected VirtualHost vhost;

    private String queueName;   //队列名称
    private boolean durable;    //是否持久化
    private boolean exclusive;  //是否只被一个客户端连接使用,且当连接关闭后,删除队列
    private boolean autoDelete; //是否自动删除(当最后一个消费者退订后即被删除)
    private Map<String, Object> Arguments;  //参放下面的一些参数
//    private int x_message_ttl;  //消息的过期时间,单位毫秒
//    private int x_max_length;   //队列最大长度,超过该最大值,则将从队列头部开始删除消息
//    private String x_overflow;  //设置队列溢出行为。这取决了当达到队列的最大长度时消息会发生什么。
//                                // 有效值是drop-dead、reject-publish或reject-publish-dlx
//    private String x_dead_letter_exchange; //死信交换机名称,过期或被删除(因队列长度超长或因空间超过阈值)的消息可指定发送到该交换机中
//    private String x_dead_letter_routing_key;   //死信消息路由器, 在消息发送到死信交换机时会使用该路由器,如果不设置,则使用消息原来的路由键值
//    private String x_single_active_consumer;    //表示队列是否是单一活动消费者,true时, 注册的消费组内只有一个消费者消费消息,其他被忽略,
//                                                //false时消息循环分发给所有消费者
//    private String x_max_priority;  //队列要支持的最大优先级数,如果未设置,队列将不支持消息优先级
//    private String x_queue_mode;    //将队列设置为延迟模式,在磁盘上保留尽可能多的消息, 以减少RAM的使用
//                                    //如果未设置, 队列将保留内存缓存以尽可能快地传递消息;


    public String getQueueName() {
        return queueName;
    }

    public Queue setQueueName(String queueName) {
        this.queueName = queueName;
        return this;
    }

    public boolean isDurable() {
        return durable;
    }

    public Queue setDurable(boolean durable) {
        this.durable = durable;
        return this;
    }

    public boolean isExclusive() {
        return exclusive;
    }

    public Queue setExclusive(boolean exclusive) {
        this.exclusive = exclusive;
        return this;
    }

    public boolean isAutoDelete() {
        return autoDelete;
    }

    public Queue setAutoDelete(boolean autoDelete) {
        this.autoDelete = autoDelete;
        return this;
    }

    public Map<String, Object> getArguments() {
        return Arguments;
    }

    public Queue setArguments(Map<String, Object> arguments) {
        Arguments = arguments;
        return this;
    }

    public VirtualHost getVhost() {
        return vhost;
    }

    public Queue setVhost(VirtualHost vhost) {
        this.vhost = vhost;
        return this;
    }


}
