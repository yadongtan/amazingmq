package com.yadong.amazingmq.client.netty.handler;

import com.yadong.amazingmq.frame.Frame;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public interface BrokerNettyClient{

    public Frame syncSend(Frame frame) throws ExecutionException, InterruptedException;

}
