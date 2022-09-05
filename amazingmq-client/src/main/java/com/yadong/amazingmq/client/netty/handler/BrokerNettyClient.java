package com.yadong.amazingmq.client.netty.handler;

import com.yadong.amazingmq.frame.Frame;

import java.util.concurrent.ExecutionException;

public interface BrokerNettyClient{

    public Frame syncSend(Frame frame) throws ExecutionException, InterruptedException;

}
