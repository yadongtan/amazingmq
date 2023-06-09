package com.yadong.amazingmq.server.netty.handler;

import com.yadong.amazingmq.frame.Frame;
import com.yadong.amazingmq.server.Commander;
import com.yadong.amazingmq.server.connection.Connection;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;


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
        //logger.info("接收到Frame:" + msg.toString());
        Frame received = (Frame) msg;
        Frame frame = null;
        frame = Commander.resolveFrame(received, this);
        if(frame != null){
            ctx.writeAndFlush(frame);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        closeConnection();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        closeConnection();
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
        connection.setClient(this);
    }

    public void sendMessage(Frame frame) {


        context.writeAndFlush(frame);
    }

    public void closeConnection(){
        if(connection != null){
            logger.info("connection[" + connection.getConnectionId() + "] 断开连接");
            connection.getVirtualHost().removeAndCloseConnection(connection.getConnectionId());
        }
    }
}
