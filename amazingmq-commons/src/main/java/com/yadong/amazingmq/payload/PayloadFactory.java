package com.yadong.amazingmq.payload;

import com.yadong.amazingmq.frame.Frame;

@Deprecated
public class PayloadFactory {

    public static Payload createPayload(Frame.PayloadType payloadType) {
        switch (payloadType) {
            case CREATE_CONNECTION:
                return new ConnectionPayload();
            case CREATE_CHANNEL:
                return new CreateChannelPayload();
        }
        return null;
    }
}
