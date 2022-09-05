package com.yadong.amazingmq.client.frame;

import com.yadong.amazingmq.client.connection.Connection;
import com.yadong.amazingmq.frame.Frame;
import com.yadong.amazingmq.utils.ObjectMapperUtils;

import java.nio.charset.StandardCharsets;

public class FrameFactory {

    public static Frame createConnectionFrame(Connection connection) {
        Frame frame = new Frame();
        frame.setType(Frame.PayloadType.CREATE_CONNECTION.getType());
        frame.setChannel((short) 0);
        String payload = ObjectMapperUtils.toJSON(connection);
        frame.setSize(payload.getBytes(StandardCharsets.UTF_8).length);
        frame.setPayload(payload);
        frame.setFrameEnd(Frame.FRAME_END);
        return frame;
    }
}
