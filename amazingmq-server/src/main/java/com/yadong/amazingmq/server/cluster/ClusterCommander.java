package com.yadong.amazingmq.server.cluster;

import com.yadong.amazingmq.frame.Frame;
import com.yadong.amazingmq.frame.Message;
import com.yadong.amazingmq.payload.*;
import com.yadong.amazingmq.payload.cluster.ClusterComponentCreatedPayload;
import com.yadong.amazingmq.server.AmazingMqBroker;
import com.yadong.amazingmq.server.Commander;
import com.yadong.amazingmq.server.bind.Binding;
import com.yadong.amazingmq.server.channel.Channel;
import com.yadong.amazingmq.server.cluster.handler.ClusterServerHandler;
import com.yadong.amazingmq.server.connection.Connection;
import com.yadong.amazingmq.server.exchange.Exchange;
import com.yadong.amazingmq.server.factory.AbstractBrokerFactory;
import com.yadong.amazingmq.server.factory.BrokerFactoryProducer;
import com.yadong.amazingmq.server.netty.handler.BrokerNettyHandler;
import com.yadong.amazingmq.server.queue.AmazingMqQueue;
import com.yadong.amazingmq.server.vhost.VirtualHost;
import com.yadong.amazingmq.utils.ObjectMapperUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class ClusterCommander {

    private static final Logger logger = LoggerFactory.getLogger(Commander.class);

    public static Frame resolveFrame(Frame frame) {
        try {
            //解析这些帧, 同步这些状态
            short type = frame.getType();

            // 创建交换机
            if (type == Frame.PayloadType.EXCHANGE_DECLARED.getType()) {
                ClusterComponentCreatedPayload payload = ObjectMapperUtils.toObject(frame.getPayload(), ClusterComponentCreatedPayload.class);
                Exchange exchange = (Exchange) BrokerFactoryProducer.createFactory(frame).create(payload.getVirtualHost(), ObjectMapperUtils.toObject(payload.getCreateComponentPayloadJson(), ExchangeDeclarePayload.class));
                VirtualHost virtualHost = AmazingMqBroker.getInstance().getVirtualHostMap().get(payload.getVirtualHost());
                exchange.setVhost(virtualHost);
                virtualHost.addExchange(exchange);
            }
            // 创建队列
            if(type == Frame.PayloadType.QUEUE_DECLARED.getType()) {
                ClusterComponentCreatedPayload payload = ObjectMapperUtils.toObject(frame.getPayload(), ClusterComponentCreatedPayload.class);
                AmazingMqQueue queue = (AmazingMqQueue) BrokerFactoryProducer.createFactory(frame).create(payload.getVirtualHost(), ObjectMapperUtils.toObject(payload.getCreateComponentPayloadJson(), QueueDeclarePayload.class));
                VirtualHost virtualHost = AmazingMqBroker.getInstance().getVirtualHostMap().get(payload.getVirtualHost());
                queue.setVhost(virtualHost);
                virtualHost.addQueue(queue);
                // 不用添加channel了吧...
//                Channel channel = client.getConnection().getChannelMap().get(frame.getChannelId());
//                channel.addQueue(queue);
            }
            // 创建绑定
            if(type == Frame.PayloadType.BINDING_DECLARED.getType()){
                ClusterComponentCreatedPayload payload = ObjectMapperUtils.toObject(frame.getPayload(), ClusterComponentCreatedPayload.class);
                Binding binding = (Binding) BrokerFactoryProducer.createFactory(frame).create(payload.getVirtualHost(), ObjectMapperUtils.toObject(payload.getCreateComponentPayloadJson(), BindingPayload.class));
                VirtualHost virtualHost = AmazingMqBroker.getInstance().getVirtualHostMap().get(payload.getVirtualHost());
                virtualHost.getExchange(binding.getExchangeName()).setBinding(binding);
                virtualHost.getBindingMap().put(binding.getRoutingKey(), binding);
            }
            //返回创建成功帧
            Frame successfulFrame = new Frame();
            successfulFrame.setChannelId(frame.getChannelId());
            successfulFrame.setFrameId(frame.getFrameId());
            successfulFrame.setType(Frame.PayloadType.SUCCESSFUL.getType());
            logger.info("返回帧:" + successfulFrame);
            return successfulFrame;
        }catch (Exception e){
            e.printStackTrace();
            // 发生错误, 返回创建发生错误帧
            Frame ackErrorFrame = new Frame();
            ackErrorFrame.setType(Frame.PayloadType.ERROR.getType());
            ackErrorFrame.setChannelId(frame.getChannelId());
            if(e.getMessage()!= null){
                ackErrorFrame.setPayload(e.getMessage());
            }
            ackErrorFrame.setFrameId(frame.getFrameId());
            return ackErrorFrame;
        }
    }

}
