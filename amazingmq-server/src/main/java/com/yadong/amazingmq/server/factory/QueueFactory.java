package com.yadong.amazingmq.server.factory;

import com.yadong.amazingmq.frame.Frame;
import com.yadong.amazingmq.payload.QueueDeclarePayload;
import com.yadong.amazingmq.server.queue.CommonQueue;
import com.yadong.amazingmq.server.queue.Queue;
import com.yadong.amazingmq.utils.ObjectMapperUtils;

public class QueueFactory extends AbstractBrokerFactory{


    QueueFactory(Frame frame) {
        super(frame);
    }

    @Override
    public Queue create() {
        String payloadJson = frame.getPayload();
        QueueDeclarePayload payload = ObjectMapperUtils.toObject(payloadJson, QueueDeclarePayload.class);
        Queue queue = new CommonQueue();
        queue
                .setQueueName(payload.getQueueName())
                .setAutoDelete(payload.isAutoDelete())
                .setDurable(payload.isDurable())
                .setExclusive(payload.isExclusive())
                .setArguments(payload.getArguments());
        return queue;

    }

}
