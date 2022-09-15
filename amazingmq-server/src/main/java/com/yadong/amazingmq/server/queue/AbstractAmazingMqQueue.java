package com.yadong.amazingmq.server.queue;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yadong.amazingmq.frame.Message;
import com.yadong.amazingmq.server.AmazingMqBroker;
import com.yadong.amazingmq.server.channel.Channel;
import com.yadong.amazingmq.server.cluster.AmazingMqClusterApplication;
import com.yadong.amazingmq.server.exchange.Exchange;
import com.yadong.amazingmq.server.exchange.NoSuchExchangeException;
import com.yadong.amazingmq.server.vhost.VirtualHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

// TODO: 2022/9/10 队列属性的设置
public abstract class AbstractAmazingMqQueue implements AmazingMqQueue {

    private static final Logger logger = LoggerFactory.getLogger(AbstractAmazingMqQueue.class);

    @JsonIgnore
    protected VirtualHost vhost;
    private List<Channel> channelListenerList = new CopyOnWriteArrayList<>(); //写时复制,读写分离,避免并发问题
    private Exchange deadLetterExchange;    //本队列设置的死信交换机
    private String deadLetterRoutingKey;        //本队列设置的死信交换机对应的routingKey

    protected BlockingQueue<Message> queue;  //实际的queue,采用不同阻塞队列实现
    private String queueName;   //队列名称
    private boolean durable;    //是否持久化
    private boolean exclusive;  //是否只被一个客户端连接使用,且当连接关闭后,删除队列
    private boolean autoDelete; //是否自动删除(当最后一个消费者退订后即被删除)
    private Map<String, Object> arguments;  //参放下面的一些参数

    private final AtomicInteger messageLength = new AtomicInteger(0); //当前队列已经存放的消息数量

    private int x_message_ttl = 0;  //消息的过期时间,单位毫秒
    private int x_max_length;   //队列最大长度,超过该最大值,则将从队列头部开始删除消息


//    private String x_overflow;  //设置队列溢出行为。这取决了当达到队列的最大长度时消息会发生什么。
//                                // 有效值是drop-dead、reject-publish或reject-publish-dlx

//    private String x_dead_letter_exchange; //死信交换机名称,过期或被删除(因队列长度超长或因空间超过阈值)的消息可指定发送到该交换机中
//    private String x_dead_letter_routing_key;   //死信消息路由器, 在消息发送到死信交换机时会使用该路由器,如果不设置,则使用消息原来的路由键值
//    private String x_single_active_consumer;    //表示队列是否是单一活动消费者,true时, 注册的消费组内只有一个消费者消费消息,其他被忽略,
//                                                //false时消息循环分发给所有消费者

    public AbstractAmazingMqQueue(String queueName, boolean autoDelete, boolean durable, boolean exclusive, Map<String,Object> arguments, VirtualHost virtualHost){
        this.queueName = queueName;
        this.autoDelete = autoDelete;
        this.durable = durable;
        this.exclusive = exclusive;
        this.arguments = arguments;
        this.vhost = virtualHost;

        // 根据不同参数 生成 不同的延迟队列
        if(arguments == null || arguments.get("x-max-length") == null){
            x_max_length = Integer.MAX_VALUE;
        }
        // 有过期时间
        if(arguments != null && arguments.get("x-message-ttl") != null){
            x_message_ttl = (int) arguments.get("x-message-ttl");
            queue = new DelayQueue<>();
        }else{
            // 无过期时间, 则使用数组有界阻塞队列
            queue = new LinkedBlockingDeque<>(x_max_length);
        }
        if(arguments != null){
            //如果有设置死信队列的参数,则设置上
            String arg_x_dead_letter_exchange = (String) arguments.get("x-dead-letter-exchange");
            if(arg_x_dead_letter_exchange != null){
                Exchange exchange = vhost.getExchange(arg_x_dead_letter_exchange);
                if(exchange == null){
                    throw new NoSuchExchangeException("没有找到对应的死信交换机:" + arg_x_dead_letter_exchange);
                }else{
                    this.deadLetterExchange = exchange;
                    this.deadLetterRoutingKey = (String) arguments.get("x-dead-letter-routing-key");
                }
            }
        }
        //QueueScheduler.getInstance().addQueue(this);
        // 启动检测是否要派发消息
        startMessageScheduler();
    }


    public String getQueueName() {
        return queueName;
    }

    public AmazingMqQueue setQueueName(String queueName) {
        this.queueName = queueName;
        return this;
    }

    public boolean isDurable() {
        return durable;
    }

    public AmazingMqQueue setDurable(boolean durable) {
        this.durable = durable;
        return this;
    }

    public boolean isExclusive() {
        return exclusive;
    }

    public AmazingMqQueue setExclusive(boolean exclusive) {
        this.exclusive = exclusive;
        return this;
    }

    public boolean isAutoDelete() {
        return autoDelete;
    }

    public AmazingMqQueue setAutoDelete(boolean autoDelete) {
        this.autoDelete = autoDelete;
        return this;
    }

    public Map<String, Object> getArguments() {
        return arguments;
    }

    public AmazingMqQueue setArguments(Map<String, Object> arguments) {
        this.arguments = arguments;
        return this;
    }

    public VirtualHost getVhost() {
        return vhost;
    }

    public AmazingMqQueue setVhost(VirtualHost vhost) {
        this.vhost = vhost;
        return this;
    }

    @Override
    public boolean offer(Message message) throws OutOfMaxLengthException {
        //有过期时间,说明使用的是延迟队列,需要判断是否达到最大值
        if (x_message_ttl != 0) {
            int nextLen = messageLength.incrementAndGet();
            if(nextLen > x_max_length){
                messageLength.decrementAndGet();
                throw new OutOfMaxLengthException("队列已满");
            }
        }
        return queue.offer(message);
    }

    // 添加一个与此队列绑定的channel
    public boolean addChannelListener(Channel channel){
        boolean added = false;
        synchronized (this) {
            if(channelListenerList == null){
                return added;
            }
            added = channelListenerList.add(channel);
        }
        return added;
    }

    //移除一个Channel,这里要判断一下如果移除后没有channel, 是否是声明了自动删除的,如果是, 则应当删除此队列
    public boolean removeChannelListener(Channel channel){
        boolean removed = channelListenerList.remove(channel);
        synchronized (this){
            if(channelListenerList.isEmpty()){
                // 如果是自动删除, 销毁
                if(autoDelete){
                    channelListenerList = null;
                    vhost.removeQueue(queueName);   //此队列移除掉
                    return true;
                }
            }
        }
        return removed;
    }

    public boolean trySendMessageToConsumer(){
        Message message = queue.peek();
        // 无法发送,先把消息继续存在队列里
        if(message == null || channelListenerList.isEmpty()) return false;
        // 可以发送,先移除掉当前消息
        while(message != null){
            for (Channel channel : channelListenerList) {
                channel.sendMessage(message);
            }
            queue.remove(message);
            message = queue.peek();
        }
        return true;
    }

    public boolean hashMessage(){
        return !queue.isEmpty();
    }


    private void startMessageScheduler(){
        new Thread(() -> {
            while (true) {
                Message message = null;
                if(channelListenerList == null){    //此队列已被移除掉
                    return;
                }
                try {
                    if (channelListenerList.isEmpty()) {
                        // TODO: 2022/9/12  改为采用异步通知的方式, 换掉死循环提升性能
                        continue;
                    }
                    message = queue.take();
                    // 如果开启了集群功能的, 在发送之前, 先判断一下其他队列是否已经发送了该消息,如果已经发送了, 那么就不应该再发送了
                    if (AmazingMqBroker.ENABLE_CLUSTER) {
                        boolean allow = AmazingMqClusterApplication.getInstance().synchronizationRemoveMessage(vhost.getPath(), queueName, message.getMessageId());
                        // 如果不允许,那么从来
                        logger.info("询问是否可以发送此消息结果:" + allow);
                        if(!allow){
                            continue;
                        }
                    }

                    logger.info("准备发送消息: message:[" + message + "]");
                    // 判断取到的消息是否超过了队列规定的最长时间, 超过了直接返回,这条消息不发了
                    if ((x_message_ttl > 0 && (System.currentTimeMillis() - message.getCreateTime() >= x_message_ttl))
                    ||  (message.getX_message_ttl() > 0 && (System.currentTimeMillis() - message.getCreateTime() >= message.getX_message_ttl()))) {
                        //被丢弃之前,先判断有无死信交换机, 如果有,转发到死信交换机上去
                        if(deadLetterExchange != null){
                            logger.info("将该消息:[" + new String(message.getContent()) + "] 转发到死信交换机:" + deadLetterExchange);
                            message.setX_message_ttl(0);
                            deadLetterExchange.sendMessageToQueue(deadLetterRoutingKey, message);
                        }else{
                            logger.info("改消息过期,被丢弃:" + new String(message.getContent()));
                        }
                        continue;
                    }
                    for (Channel channel : channelListenerList) {
                        channel.sendMessage(message);
                        channelListenerList.remove(channel);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public boolean clusterTryRemoveMessage(int messageId){
        // 取头元素出来看看
        Message msg = queue.peek();
        if(msg != null && msg.getMessageId() == messageId){
            Message take = queue.poll();    //移除这个消息
            logger.info("其他队列正在发送消息, 将移除本队列消息");
            return true;
        }else{
            logger.info("移除消息失败");
            return false;
        }
    }
}
