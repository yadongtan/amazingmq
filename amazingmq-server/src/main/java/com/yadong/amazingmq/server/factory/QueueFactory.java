package com.yadong.amazingmq.server.factory;

import com.yadong.amazingmq.frame.Frame;
import com.yadong.amazingmq.payload.QueueDeclarePayload;
import com.yadong.amazingmq.server.queue.CommonAmazingMqQueue;
import com.yadong.amazingmq.server.queue.AmazingMqQueue;
import com.yadong.amazingmq.utils.ObjectMapperUtils;

public class QueueFactory extends AbstractBrokerFactory{


    QueueFactory(Frame frame) {
        super(frame);
    }

    @Override
    public AmazingMqQueue create() {
        String payloadJson = frame.getPayload();
        QueueDeclarePayload payload = ObjectMapperUtils.toObject(payloadJson, QueueDeclarePayload.class);
        AmazingMqQueue queue = new CommonAmazingMqQueue(
                payload.getQueueName(),
                payload.isAutoDelete(),
                payload.isDurable(),
                payload.isExclusive(),
                payload.getArguments()
                );
        return queue;

    }

}
