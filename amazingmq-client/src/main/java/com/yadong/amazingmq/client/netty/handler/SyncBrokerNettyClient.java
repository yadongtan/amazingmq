package com.yadong.amazingmq.client.netty.handler;


import com.yadong.amazingmq.client.channel.Channel;
import com.yadong.amazingmq.client.connection.Connection;
import com.yadong.amazingmq.frame.Frame;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
* @author YadongTan
* @date 2022/9/5 18:19
* @Description 抽象出来的,用来异步操作client发送消息到Broker的
*/
public abstract class SyncBrokerNettyClient extends ChannelInboundHandlerAdapter implements BrokerNettyClient{

    private static final ExecutorService threadPool =
            Executors.newFixedThreadPool(4);

    @Override
    public Frame syncSend(Channel channel, Frame frame) throws ExecutionException, InterruptedException {
        Frame result = threadPool.submit(() -> {
            return send(channel, frame);
        }).get();
        return result;
    }


    // implement by subclasses!
    protected Frame send(Channel channel,Frame frame) throws InterruptedException {
        throw new UnsupportedOperationException();
    }

}
