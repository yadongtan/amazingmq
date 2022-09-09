package com.yadong.amazingmq.server.factory;

import com.yadong.amazingmq.frame.Frame;
import com.yadong.amazingmq.payload.BindingPayload;
import com.yadong.amazingmq.server.bind.Binding;
import com.yadong.amazingmq.server.bind.CommonBinding;
import com.yadong.amazingmq.server.netty.handler.BrokerNettyHandler;
import com.yadong.amazingmq.utils.ObjectMapperUtils;

public class BindingFactory extends AbstractBrokerFactory{

    BindingFactory(Frame frame) {
        super(frame);
    }

    @Override
    public Binding create(BrokerNettyHandler client) {
        Binding binding = new CommonBinding();
        BindingPayload payload = ObjectMapperUtils.toObject(frame.getPayload(), BindingPayload.class);
        if(client.getConnection().getVirtualHost().getBindingMap().get(payload.getRoutingKey()) != null){
            return null;
        }
        binding.setExchangeName(payload.getExchangeName())
                .setQueueName(payload.getQueueName())
                .setRoutingKey(payload.getRoutingKey());
        return binding;
    }
}
