package com.yadong.amazingmq.server.factory;

import com.yadong.amazingmq.frame.Frame;
import com.yadong.amazingmq.payload.QueueDeclarePayload;
import com.yadong.amazingmq.server.netty.handler.BrokerNettyHandler;
import com.yadong.amazingmq.server.queue.CommonAmazingMqQueue;
import com.yadong.amazingmq.server.queue.AmazingMqQueue;
import com.yadong.amazingmq.utils.ObjectMapperUtils;

public class QueueFactory extends AbstractBrokerFactory{


    QueueFactory(Frame frame) {
        super(frame);
    }

    @Override
    public AmazingMqQueue create(BrokerNettyHandler client) {
        String payloadJson = frame.getPayload();
        QueueDeclarePayload payload = ObjectMapperUtils.toObject(payloadJson, QueueDeclarePayload.class);

        if(client.getConnection().getVirtualHost().getQueue(payload.getQueueName()) != null){
            return null;
        }
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
