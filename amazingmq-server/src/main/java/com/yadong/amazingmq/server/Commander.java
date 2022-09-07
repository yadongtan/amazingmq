package com.yadong.amazingmq.server;

import com.sun.net.httpserver.Authenticator;
import com.yadong.amazingmq.frame.Frame;
import com.yadong.amazingmq.server.factory.AbstractBrokerFactory;
import com.yadong.amazingmq.server.factory.BrokerFactoryProducer;
import org.omg.PortableInterceptor.SUCCESSFUL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Commander {

    private static final Logger logger = LoggerFactory.getLogger(Commander.class);

    public static Frame resolveFrame(Frame frame) {

        try {
            // 创建相关组件的操作
            if (frame.getType() < Frame.PayloadType.CREATE_ID_MAX.getType()) {
                BrokerFactoryProducer.createFactory(frame).create();
            }
            Frame successfulFrame = new Frame();
            successfulFrame.setFrameId(frame.getFrameId());
            successfulFrame.setType(Frame.PayloadType.SUCCESSFUL.getType());
            return successfulFrame;
        }catch (Exception e){
            // 发生错误
            Frame ackErrorFrame = new Frame();
            ackErrorFrame.setType(Frame.PayloadType.ERROR.getType());
            ackErrorFrame.setPayload(e.getMessage());
            ackErrorFrame.setFrameId(frame.getFrameId());
            return ackErrorFrame;
        }
    }
}
