package com.yadong.amazingmq.proxy.netty.handler;

import com.yadong.amazingmq.frame.Frame;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
* @author YadongTan
* @date 2022/9/15 11:56
* @Description 集群间通信的client
*/
public class ProxyClientHandler extends ChannelInboundHandlerAdapter {


    private ChannelHandlerContext context;
    private final ChannelHandlerContext acceptorContext;


    public ProxyClientHandler(ChannelHandlerContext context){
        this.acceptorContext = context;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context = ctx;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        acceptorContext.writeAndFlush(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    public void send(Frame frame){
        context.writeAndFlush(frame);
    }
}
