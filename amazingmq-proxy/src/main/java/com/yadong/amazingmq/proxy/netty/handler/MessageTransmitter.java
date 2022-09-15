package com.yadong.amazingmq.proxy.netty.handler;


import com.yadong.amazingmq.frame.Frame;
import com.yadong.amazingmq.proxy.AmazingProxy;
import com.yadong.amazingmq.proxy.loadbalance.LoadBalance;
import com.yadong.amazingmq.proxy.loadbalance.LoadBalanceFactory;
import com.yadong.amazingmq.property.HostInfo;
import com.yadong.amazingmq.proxy.netty.ProxyNettyClient;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageTransmitter {

    private ChannelHandlerContext context;

    private static final ExecutorService threadPool = Executors.newFixedThreadPool(64);
    private ProxyClientHandler client;
    private boolean reset = false;


    public void establishConnection(ChannelHandlerContext context) throws InterruptedException {
        this.context = context;
        LoadBalance loadBalance = LoadBalanceFactory.getLoadBalance(AmazingProxy.loadBalanceStrategy);
        HostInfo hostInfo = loadBalance.doSelect(AmazingProxy.getInstance().getHostList());
        client = ProxyNettyClient.createAndConnect(this, context, hostInfo);
    }

    public void transmitMessage(ChannelHandlerContext context, Frame frame){
        //发送请求, 等待回调
        if(reset){
            //需要重设连接
            Frame resetFrame = new Frame();
            resetFrame.setFrameId(frame.getFrameId());
            resetFrame.setType(Frame.PayloadType.RESET_CONNECTION.getType());
            context.writeAndFlush(resetFrame);
            reset = false;
            return;
        }
        client.send(frame);
    }

    public void reconnectClient() throws InterruptedException {
        establishConnection(this.context);
    }

}
