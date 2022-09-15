package com.yadong.amazingmq.server.factory;

import com.yadong.amazingmq.frame.Frame;
import com.yadong.amazingmq.payload.Payload;
import com.yadong.amazingmq.payload.QueueDeclarePayload;
import com.yadong.amazingmq.server.AmazingMqBroker;
import com.yadong.amazingmq.server.netty.handler.BrokerNettyHandler;
import com.yadong.amazingmq.server.queue.CommonAmazingMqQueue;
import com.yadong.amazingmq.server.queue.AmazingMqQueue;
import com.yadong.amazingmq.server.vhost.AuthErrorException;
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
                payload.getArguments(),
                client.getConnection().getVirtualHost()
                );
        return queue;

    }

    @Override
    public AmazingMqQueue create(String virtualHost) {
        String payloadJson = frame.getPayload();
        QueueDeclarePayload payload = ObjectMapperUtils.toObject(payloadJson, QueueDeclarePayload.class);

        if(AmazingMqBroker.getInstance().getVirtualHostMap().get(virtualHost).getQueue(payload.getQueueName()) != null){
            return null;
        }
        AmazingMqQueue queue = new CommonAmazingMqQueue(
                payload.getQueueName(),
                payload.isAutoDelete(),
                payload.isDurable(),
                payload.isExclusive(),
                payload.getArguments(),
                AmazingMqBroker.getInstance().getVirtualHostMap().get(virtualHost)
        );
        return queue;

    }

    @Override
    public Object create(String virtualHost, Payload payload) throws AuthErrorException {
        QueueDeclarePayload queueDeclarePayload = (QueueDeclarePayload) payload;

        if(AmazingMqBroker.getInstance().getVirtualHostMap().get(virtualHost).getQueue(queueDeclarePayload.getQueueName()) != null){
            return null;
        }
        AmazingMqQueue queue = new CommonAmazingMqQueue(
                queueDeclarePayload.getQueueName(),
                queueDeclarePayload.isAutoDelete(),
                queueDeclarePayload.isDurable(),
                queueDeclarePayload.isExclusive(),
                queueDeclarePayload.getArguments(),
                AmazingMqBroker.getInstance().getVirtualHostMap().get(virtualHost)
        );
        return queue;
    }

}
