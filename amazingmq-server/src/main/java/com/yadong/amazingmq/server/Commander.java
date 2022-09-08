package com.yadong.amazingmq.server;

import com.yadong.amazingmq.frame.Frame;
import com.yadong.amazingmq.frame.Message;
import com.yadong.amazingmq.payload.PublishMessagePayload;
import com.yadong.amazingmq.server.bind.Binding;
import com.yadong.amazingmq.server.channel.Channel;
import com.yadong.amazingmq.server.connection.Connection;
import com.yadong.amazingmq.server.exchange.Exchange;
import com.yadong.amazingmq.server.factory.BrokerFactoryProducer;
import com.yadong.amazingmq.server.netty.handler.BrokerNettyHandler;
import com.yadong.amazingmq.server.queue.AmazingMqQueue;
import com.yadong.amazingmq.utils.ObjectMapperUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Commander {

    private static final Logger logger = LoggerFactory.getLogger(Commander.class);

    public static Frame resolveFrame(Frame frame, BrokerNettyHandler client) {

        try {
            // 创建相关组件的操作
            if (frame.getType() < Frame.PayloadType.CREATE_ID_MAX.getType()) {
                Object component = BrokerFactoryProducer.createFactory(frame).create();
                // 创建连接
                if(frame.getType() == Frame.PayloadType.CREATE_CONNECTION.getType()){
                    client.setConnection((Connection) component);
                    // 创建信道
                }else if(frame.getType() == Frame.PayloadType.CREATE_CHANNEL.getType()){
                    client.getConnection().addChannel((Channel) component);
                    ((Channel) component).setConnection(client.getConnection());
                    // 声明交换机
                }else if(frame.getType() == Frame.PayloadType.EXCHANGE_DECLARED.getType()){
                    Exchange exchange = (Exchange) component;
                    exchange.setVhost(client.getConnection().getVirtualHost());
                    client.getConnection().getVirtualHost().addExchange(exchange);
                    // 声明队列
                }else if(frame.getType() == Frame.PayloadType.QUEUE_DECLARED.getType()){
                    AmazingMqQueue queue = (AmazingMqQueue) component;
                    queue.setVhost(client.getConnection().getVirtualHost());
                    client.getConnection().getVirtualHost().addQueue(queue);
                    // 创建绑定
                } else if (frame.getType() == Frame.PayloadType.BINDING_DECLARED.getType()) {
                    Binding binding = (Binding) component;
                    client.getConnection().getVirtualHost().getExchange(binding.getExchangeName()).setBinding(binding);
                    client.getConnection().getVirtualHost().getBindingMap().put(binding.getRoutingKey(), binding);
                }
                //生产者的相关类型帧
            } else if (frame.getType() > Frame.PayloadType.CREATE_ID_MAX.getType() && frame.getType() < Frame.PayloadType.CONSUMER_MAX.getType()) {
                //发布消息
                if (frame.getType() == Frame.PayloadType.BASIC_PUBLISH.getType()) {
                    short channelId = frame.getChannelId();
                    PublishMessagePayload payload = ObjectMapperUtils.toObject(frame.getPayload(), PublishMessagePayload.class);
                    Message message = payload.getMessage();
                    Exchange exchange = client.getConnection().getVirtualHost().getExchange(payload.getExchangeName());
                    exchange.sendMessageToQueue(payload.getRoutingKey(), message);
                }
            }

            //返回创建成功帧
            Frame successfulFrame = new Frame();
            successfulFrame.setFrameId(frame.getFrameId());
            successfulFrame.setType(Frame.PayloadType.SUCCESSFUL.getType());
            logger.info("返回帧:" + successfulFrame);
            return successfulFrame;
        }catch (Exception e){
            e.printStackTrace();
            // 发生错误, 返回创建发生错误帧
            Frame ackErrorFrame = new Frame();
            ackErrorFrame.setType(Frame.PayloadType.ERROR.getType());
            if(e.getMessage()!= null){
                ackErrorFrame.setPayload(e.getMessage());
            }
            ackErrorFrame.setFrameId(frame.getFrameId());
            return ackErrorFrame;
        }
    }
}
