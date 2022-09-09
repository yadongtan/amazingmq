package com.yadong.amazingmq.client.netty.handler;

import com.yadong.amazingmq.client.channel.AMQChannel;
import com.yadong.amazingmq.client.channel.Channel;
import com.yadong.amazingmq.client.exception.ConcurrentFrameOutOfLimitException;
import com.yadong.amazingmq.frame.Frame;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
* @author YadongTan
* @date 2022/9/4 23:03
* @Description 启动Broker, 用户在连接以后, 生成一个Connection

*/
public class BrokerNettyClientHandler extends SyncBrokerNettyClient {

    private static final Logger logger = LoggerFactory.getLogger(BrokerNettyClientHandler.class);

    private final Channel[] channelLocks = new Channel[65536];    //并发调用时用来降低锁粒度的数组
    private final Frame[] frameResult = new Frame[65536]; //返回结果
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
        //context.writeAndFlush(new Frame((short)1, (short)2022, 4, "test",'\r'));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("Client 收到 Broker 返回帧: " + msg.toString());
        Frame receivedFrame = (Frame) msg;
        int index = receivedFrame.getChannelId() & (65536 - 1);
        Channel fromChannel = channelLocks[index];
        synchronized (fromChannel){
            fromChannel.notify(); // 唤醒原来线程, 返回结果
            frameResult[index] = receivedFrame;
        }
    }

    @Override
    public Frame send(Channel channel, Frame frame) throws InterruptedException {
        if (channel == null) {
            frame.setChannelId((short)0);
            // 创建一个空channel,当作锁
            channel = new AMQChannel(0, null, null);
        }
        int index = frame.getChannelId() & (65536 - 1);
        channelLocks[index] = channel;
        synchronized (channel){
            context.writeAndFlush(frame);
            channel.wait();
        }
        Frame receivedFrame = frameResult[index];   //取出结果
        channelLocks[index] = null; //锁赋为null
        return receivedFrame;
    }


}
