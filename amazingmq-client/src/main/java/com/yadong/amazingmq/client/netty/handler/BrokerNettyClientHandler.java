package com.yadong.amazingmq.client.netty.handler;

import com.sun.corba.se.impl.orbutil.concurrent.Sync;
import com.yadong.amazingmq.frame.Frame;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Reader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
* @author YadongTan
* @date 2022/9/4 23:03
* @Description 启动Broker, 用户在连接以后, 生成一个Connection

*/
public class BrokerNettyClientHandler extends SyncBrokerNettyClient {

    private static final Logger logger = LoggerFactory.getLogger(BrokerNettyClientHandler.class);

    private ChannelHandlerContext context;

    // 怎么才能保证一个线程拿到自己的那份呢

    //与服务器建立连接之后, 此方法被调用, 设置好上下文
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context = ctx;
        logger.info("Client 与 Broker 建立连接");
        context.writeAndFlush(new Frame((short)1, (short)2022, 4, "test",'\r'));
    }

    @Override
    public Frame send(Frame frame) {
        context.writeAndFlush(frame);
        logger.info("得到结果...");
        return null;
    }

}
