package com.yadong.amazingmq.server.cluster;

import com.yadong.amazingmq.frame.Frame;
import com.yadong.amazingmq.frame.Message;
import com.yadong.amazingmq.payload.*;
import com.yadong.amazingmq.payload.cluster.ClusterMessageRemovedPayload;
import com.yadong.amazingmq.payload.cluster.ClusterTransmitPayload;
import com.yadong.amazingmq.server.AmazingMqBroker;
import com.yadong.amazingmq.server.Commander;
import com.yadong.amazingmq.server.bind.Binding;
import com.yadong.amazingmq.server.exchange.Exchange;
import com.yadong.amazingmq.server.factory.BrokerFactoryProducer;
import com.yadong.amazingmq.server.queue.AmazingMqQueue;
import com.yadong.amazingmq.server.vhost.VirtualHost;
import com.yadong.amazingmq.utils.ObjectMapperUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClusterCommander {

    private static final Logger logger = LoggerFactory.getLogger(Commander.class);

    public static Frame resolveFrame(Frame frame) {
        try {
            //解析这些帧, 同步这些状态
            short type = frame.getType();
            //返回创建成功帧
            Frame successfulFrame = new Frame();
            successfulFrame.setChannelId(frame.getChannelId());
            successfulFrame.setFrameId(frame.getFrameId());
            successfulFrame.setType(Frame.PayloadType.SUCCESSFUL.getType());
            // 创建交换机
            if (type == Frame.PayloadType.EXCHANGE_DECLARED.getType()) {
                ClusterTransmitPayload payload = ObjectMapperUtils.toObject(frame.getPayload(), ClusterTransmitPayload.class);
                Exchange exchange = (Exchange) BrokerFactoryProducer.createFactory(frame).create(payload.getVirtualHost(), ObjectMapperUtils.toObject(payload.getOriginalPayloadJson(), ExchangeDeclarePayload.class));
                if(exchange != null){
                    VirtualHost virtualHost = AmazingMqBroker.getInstance().getVirtualHostMap().get(payload.getVirtualHost());
                    exchange.setVhost(virtualHost);
                    virtualHost.addExchange(exchange);
                }
            }
            // 创建队列
            if(type == Frame.PayloadType.QUEUE_DECLARED.getType()) {
                ClusterTransmitPayload payload = ObjectMapperUtils.toObject(frame.getPayload(), ClusterTransmitPayload.class);
                AmazingMqQueue queue = (AmazingMqQueue) BrokerFactoryProducer.createFactory(frame).create(payload.getVirtualHost(), ObjectMapperUtils.toObject(payload.getOriginalPayloadJson(), QueueDeclarePayload.class));
                if(queue != null){
                    VirtualHost virtualHost = AmazingMqBroker.getInstance().getVirtualHostMap().get(payload.getVirtualHost());
                    queue.setVhost(virtualHost);
                    virtualHost.addQueue(queue);
                    // 不用添加channel了吧...
//                Channel channel = client.getConnection().getChannelMap().get(frame.getChannelId());
//                channel.addQueue(queue);
                }
            }
            // 创建绑定
            if(type == Frame.PayloadType.BINDING_DECLARED.getType()){
                ClusterTransmitPayload payload = ObjectMapperUtils.toObject(frame.getPayload(), ClusterTransmitPayload.class);
                Binding binding = (Binding) BrokerFactoryProducer.createFactory(frame).create(payload.getVirtualHost(), ObjectMapperUtils.toObject(payload.getOriginalPayloadJson(), BindingPayload.class));
                if(binding != null){
                    VirtualHost virtualHost = AmazingMqBroker.getInstance().getVirtualHostMap().get(payload.getVirtualHost());
                    virtualHost.getExchange(binding.getExchangeName()).setBinding(binding);
                    virtualHost.getBindingMap().put(binding.getRoutingKey(), binding);
                }
            }
            // 发布消息,同步消息
            if(type == Frame.PayloadType.BASIC_PUBLISH.getType()){
                ClusterTransmitPayload payload = ObjectMapperUtils.toObject(frame.getPayload(), ClusterTransmitPayload.class);
                PublishMessagePayload messagePayload = ObjectMapperUtils.toObject(payload.getOriginalPayloadJson(), PublishMessagePayload.class);
                Message message = messagePayload.getMessage();
                Exchange exchange = AmazingMqBroker.getInstance().getVirtualHostMap().get(payload.getVirtualHost()).getExchange(messagePayload.getExchangeName());
                exchange.sendMessageToQueue(messagePayload.getRoutingKey(), message);
            }
            // 集群间同步移除消息
            if(type == Frame.PayloadType.REMOVE_MESSAGE.getType()){
                ClusterMessageRemovedPayload payload = ObjectMapperUtils.toObject(frame.getPayload(), ClusterMessageRemovedPayload.class);
                String vhost = payload.getVhost();
                VirtualHost virtualHost = AmazingMqBroker.getInstance().getVirtualHostMap().get(vhost);
                AmazingMqQueue queue = virtualHost.getQueue(payload.getQueueName());
                boolean result = queue.clusterTryRemoveMessage(payload.getMessageId());
                if(result){
                    //发送移除成功
                    successfulFrame.setType(Frame.PayloadType.REMOVED_MESSAGE_SUCCESSFULLY.getType());
                }
            }
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
