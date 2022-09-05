package com.yadong.amazingmq.server.netty.handler;

import com.yadong.amazingmq.frame.Frame;
import com.yadong.amazingmq.server.connection.Connection;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
* @author YadongTan
* @date 2022/9/4 23:03
* @Description 启动Broker, 用户在连接以后, 生成一个Connection

*/
public class BrokerNettyHandler extends ChannelInboundHandlerAdapter{

    private static final Logger logger = LoggerFactory.getLogger(BrokerNettyHandler.class);


    private ChannelHandlerContext context;

    private Connection connection;

    //与服务器建立连接之后, 此方法被调用, 设置好上下文
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context = ctx;
        logger.info("Broker 与 Client 建立连接");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("接收到Frame:" + msg.toString());

    }

}
