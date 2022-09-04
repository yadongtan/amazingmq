package com.yadong.amazingmq.server.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* @author YadongTan
* @date 2022/9/4 23:03
* @Description 启动Broker, 用户在连接以后, 生成一个Connection
*/
public class BrokerNettyHandler extends ChannelInboundHandlerAdapter{

    private static final Logger logger = LoggerFactory.getLogger(BrokerNettyHandler.class);

    private ChannelHandlerContext context;

    //与服务器建立连接之后, 此方法被调用, 设置好上下文
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context = ctx;
        logger.info("[Monitor] 客户端与监控中心建立连接");
    }

}
