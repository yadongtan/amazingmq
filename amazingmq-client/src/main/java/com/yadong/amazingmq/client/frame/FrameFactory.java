package com.yadong.amazingmq.client.frame;

import com.yadong.amazingmq.client.channel.AMQChannel;
import com.yadong.amazingmq.client.channel.Channel;
import com.yadong.amazingmq.client.connection.Connection;
import com.yadong.amazingmq.frame.Frame;
import com.yadong.amazingmq.frame.Message;
import com.yadong.amazingmq.payload.*;
import com.yadong.amazingmq.utils.ObjectMapperUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class FrameFactory {

    public static Frame createConnectionFrame(Connection connection) {
        Frame frame = new Frame();
        // 指定帧类型为创建Connection类型
        frame.setType(Frame.PayloadType.CREATE_CONNECTION.getType());
        ConnectionPayload connectionPayload = new ConnectionPayload();
        //创建payload
        connectionPayload
                .setUsername(connection.getUsername())
                .setPassword(connection.getPassword())
                .setVhost(connection.getVirtualHost());
        frame.setPayload(ObjectMapperUtils.toJSON(connectionPayload));
        return frame;
    }

    public static Frame createChannelFrame(AMQChannel channel){
        short channelId = (short) channel.getChannelNumber();
        CreateChannelPayload payload = new CreateChannelPayload();
        payload.setChannelId(channelId);
        payload.setConnectionId(channel.getConnection().getConnectionId());

        Frame frame = new Frame();
        frame.setChannelId(channelId);
        frame.setType(Frame.PayloadType.CREATE_CHANNEL.getType());
        frame.setPayload(ObjectMapperUtils.toJSON(payload));
        return frame;
    }

    public static Frame createExchangeDeclaredFrame(Channel channel, String exchangeName, String mode, boolean duration) {
        short channelId = (short) channel.getChannelNumber();
        ExchangeDeclarePayload payload = new ExchangeDeclarePayload();
        payload.setExchangeName(exchangeName)
                .setExchangeType(mode)
                .setDuration(duration);

        Frame frame = new Frame();
        frame.setType(Frame.PayloadType.EXCHANGE_DECLARED.getType());
        frame.setChannelId(channelId);
        frame.setPayload(ObjectMapperUtils.toJSON(payload));
        return frame;
    }

    public static Frame createQueueDeclaredFrame(
            Channel channel, String queueName,
            boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments){
        short channelId = (short) channel.getChannelNumber();
        QueueDeclarePayload payload = new QueueDeclarePayload();
        payload.setQueueName(queueName)
                .setDurable(durable)
                .setExclusive(exclusive)
                .setAutoDelete(autoDelete)
                .setArguments(arguments);
        Frame frame = new Frame();
        frame.setChannelId(channelId);
        frame.setType(Frame.PayloadType.QUEUE_DECLARED.getType());
        frame.setPayload(ObjectMapperUtils.toJSON(payload));
        return frame;
    }

    public static Frame createBindingFrame(
            Channel channel, String queueName,String exchangeName, String routingKey){
        short channelId = (short) channel.getChannelNumber();
        BindingPayload payload = new BindingPayload();

        payload.
                setQueueName(queueName)
                .setExchangeName(exchangeName)
                .setRoutingKey(routingKey);

        Frame frame = new Frame();
        frame.setChannelId(channelId);
        frame.setType(Frame.PayloadType.BINDING_DECLARED.getType());
        frame.setPayload(ObjectMapperUtils.toJSON(payload));
        return frame;
    }

    public static Frame createBasicPublishFrame(
            Channel channel, String exchangeName, String routingKey, Map<String, Object> basicProperties, byte[] messageBodyBytes){
        short channelId = (short) channel.getChannelNumber();
        PublishMessagePayload payload = new PublishMessagePayload();
        payload.setExchangeName(exchangeName);
        payload.setRoutingKey(routingKey);
        Message message = new Message(messageBodyBytes);
        if(basicProperties != null){
            Object ttl = basicProperties.get("x-message-ttl");
            if(ttl  != null){
                message.setX_message_ttl((int)ttl);
            }
        }
        payload.setMessage(message);
        Frame frame = new Frame();
        frame.setChannelId(channelId);
        frame.setType(Frame.PayloadType.BASIC_PUBLISH.getType());
        frame.setPayload(ObjectMapperUtils.toJSON(payload));
        return frame;
    }

    public static Frame createBasicConsumeFrame(Channel channel, String queueName) {
        short channelId = (short) channel.getChannelNumber();
        ConsumeMessagePayload payload = new ConsumeMessagePayload();
        payload.setQueueName(queueName);
        Frame frame = new Frame();
        frame.setChannelId(channelId);
        frame.setType(Frame.PayloadType.BASIC_CONSUME.getType());
        frame.setPayload(ObjectMapperUtils.toJSON(payload));
        return frame;
    }


    public static Frame createCloseChannelFrame(Channel channel){
        short channelId = (short) channel.getChannelNumber();
        Frame frame = new Frame();
        frame.setChannelId(channelId);
        frame.setType(Frame.PayloadType.CLOSE_CHANNEL.getType());
        return frame;
    }


    public static void main(String[] args) {
        byte[] byes = new String("\r\t").getBytes(StandardCharsets.UTF_8);
        System.out.println("....");
    }
}
