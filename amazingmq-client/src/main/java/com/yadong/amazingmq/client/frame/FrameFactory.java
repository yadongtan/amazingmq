package com.yadong.amazingmq.client.frame;

import com.yadong.amazingmq.client.channel.AMQChannel;
import com.yadong.amazingmq.client.connection.Connection;
import com.yadong.amazingmq.frame.Frame;
import com.yadong.amazingmq.payload.ConnectionPayload;
import com.yadong.amazingmq.payload.CreateChannelPayload;
import com.yadong.amazingmq.utils.ObjectMapperUtils;

import java.nio.charset.StandardCharsets;

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
        frame.setType(Frame.PayloadType.CREATE_CHANNEL.getType());
        frame.setPayload(ObjectMapperUtils.toJSON(payload));
        return frame;
    }
}
