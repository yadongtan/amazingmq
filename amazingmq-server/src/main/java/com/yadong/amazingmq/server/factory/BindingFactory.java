package com.yadong.amazingmq.server.factory;

import com.yadong.amazingmq.frame.Frame;
import com.yadong.amazingmq.server.bind.Binding;

public class BindingFactory extends AbstractBrokerFactory{

    BindingFactory(Frame frame) {
        super(frame);
    }

    @Override
    public Binding create() {
        return null;
    }
}
