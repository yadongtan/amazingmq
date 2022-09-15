package com.yadong.amazingmq.proxy.netty.handler;


import com.yadong.amazingmq.frame.Frame;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
* @author YadongTan
* @date 2022/9/15 11:42
* @Description 这个是与客户端连接的Server
*/
public class AmazingProxyHandler extends ChannelInboundHandlerAdapter {

    private ChannelHandlerContext context;
    private MessageTransmitter transmitter;

    //与服务器建立连接之后, 此方法被调用, 设置好上下文
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context = ctx;
        transmitter = new MessageTransmitter();
        transmitter.establishConnection(context);   //经过负载均衡建立一个到mq的连接
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //logger.info("接收到Frame:" + msg.toString());
        Frame received = (Frame) msg;
        //转发消息
        transmitter.transmitMessage(context, received);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    }

}
