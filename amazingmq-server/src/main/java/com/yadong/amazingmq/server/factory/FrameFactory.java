package com.yadong.amazingmq.server.factory;

import com.yadong.amazingmq.frame.Frame;
import com.yadong.amazingmq.frame.Message;
import com.yadong.amazingmq.payload.DeliverMessageToConsumerPayload;
import com.yadong.amazingmq.server.channel.Channel;
import com.yadong.amazingmq.utils.ObjectMapperUtils;

public class FrameFactory {

    public static Frame createDeliverMsgToConsumerFrame(Channel channel , Message message){
        DeliverMessageToConsumerPayload payload = new DeliverMessageToConsumerPayload();
        payload.setMessage(message);
        Frame frame = new Frame();
        frame.setChannelId(channel.getChannelId());
        frame.setType(Frame.PayloadType.DELIVER_MESSAGE.getType());
        frame.setPayload(ObjectMapperUtils.toJSON(payload));
        return frame;
    }
}
