package com.yadong.amazingmq.client.channel;

import com.yadong.amazingmq.client.connection.Connection;
import com.yadong.amazingmq.client.frame.FrameFactory;
import com.yadong.amazingmq.client.netty.handler.BrokerNettyClient;
import com.yadong.amazingmq.frame.Frame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ExecutionException;


public class AMQChannel implements Channel{

    private static final Logger logger = LoggerFactory.getLogger(AMQChannel.class);

    private short channelId;
    private final Connection connection;
    private final BrokerNettyClient client;



    public AMQChannel(Integer channelId, Connection connection, BrokerNettyClient client){
        this.connection = connection;
        this.channelId = channelId.shortValue();
        this.client = client;
    }

    @Override
    public int getChannelNumber() {
        return channelId;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public boolean exchangeDeclare(String exchangeName, String mode, boolean duration) throws ExecutionException, InterruptedException {
        Frame frame = client.syncSend(FrameFactory.createExchangeDeclaredFrame(this, exchangeName, mode, duration));
        if(frame.getType() == Frame.PayloadType.SUCCESSFUL.getType()){
            logger.info(" 声明交换机 [" + exchangeName + "] 成功");
            return true;
        }else{
            logger.info(" 声明交换机 [" + exchangeName + "] 失败");
            return false;
        }
    }

    @Override
    public boolean queueDeclare(String queueName, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments) throws ExecutionException, InterruptedException {
        Frame frame = client.syncSend(FrameFactory.createQueueDeclaredFrame(this, queueName, durable, exclusive, autoDelete, arguments));
        if(frame.getType() == Frame.PayloadType.SUCCESSFUL.getType()){
            logger.info(" 声明队列 [" + queueName + "] 成功");
            return true;
        }else{
            logger.info(" 声明队列 [" + queueName + "] 失败");
            return false;
        }
    }

    @Override
    public boolean queueBind(String queueName, String exchangeName, String routingKey) throws ExecutionException, InterruptedException {
        Frame frame = client.syncSend(FrameFactory.createBindingFrame(this, queueName, exchangeName, routingKey));
        if(frame.getType() == Frame.PayloadType.SUCCESSFUL.getType()){
            logger.info(" 创建绑定 [" + queueName + "] 成功");
            return true;
        }else{
            logger.info(" 创建绑定 [" + queueName + "] 失败");
            return false;
        }
    }
}
