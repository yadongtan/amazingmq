package com.yadong.amazingmq.client.netty.handler;

import com.sun.corba.se.impl.orbutil.concurrent.Sync;
import com.yadong.amazingmq.client.connection.Connection;
import com.yadong.amazingmq.client.exception.ConcurrentFrameOutOfLimitException;
import com.yadong.amazingmq.frame.Frame;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Reader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

/**
* @author YadongTan
* @date 2022/9/4 23:03
* @Description 启动Broker, 用户在连接以后, 生成一个Connection

*/
public class BrokerNettyClientHandler extends SyncBrokerNettyClient {

    private static final Logger logger = LoggerFactory.getLogger(BrokerNettyClientHandler.class);

    private final Frame[] framesLockArray = new Frame[65536];    //并发调用时用来降低锁粒度的数组

    private ChannelHandlerContext context;

    // 怎么才能保证一个线程拿到自己的那份呢, 当前对象是一个Connection所独有的,
    // 因此要区分Connection内的Channel,而Channel又是一个线程私有的(理论上,用户不应该让线程共享)
    // 因此, Connection - N个Channel ### 一个Channel对应一个线程, 因此可以用Channel作为锁
    // 此处选择Frame[] 数组作为锁, 利用空间换时间

    //与服务器建立连接之后, 此方法被调用, 设置好上下文
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context = ctx;
        logger.info("Client 与 Broker 建立连接");
        context.writeAndFlush(new Frame((short)1, (short)2022, 4, "test",'\r'));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("Client 收到 Broker 返回帧: " + msg.toString());
        Frame receivedFrame = (Frame) msg;
        int index = receivedFrame.getFrameId() & (65536 - 1);
        Frame sentFrame = framesLockArray[index];
        synchronized (sentFrame){
            sentFrame.notify(); // 唤醒原来线程, 返回结果
            framesLockArray[index] = receivedFrame;
        }
    }

    @Override
    public Frame send(Frame frame) throws InterruptedException {
        int index = frame.getFrameId() & (65536 - 1);
        if(framesLockArray[index] != null){
            throw new ConcurrentFrameOutOfLimitException("并发越过阈值");
        }
        framesLockArray[index] = frame;
        context.writeAndFlush(frame);
        synchronized (frame){
            frame.wait();
        }
        Frame receivedFrame = framesLockArray[index];
        framesLockArray[index] = null;
        return receivedFrame;
    }

}
