package com.yadong.amazingmq.server.cluster.handler;

import com.yadong.amazingmq.frame.Frame;
import com.yadong.amazingmq.property.HostInfo;
import com.yadong.amazingmq.server.cluster.AmazingMqClusterApplication;
import com.yadong.amazingmq.server.cluster.MessageResponseCount;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.omg.PortableInterceptor.SUCCESSFUL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* @author YadongTan
* @date 2022/9/15 13:41
* @Description 提供集群通信
*/
public class ClusterClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ClusterClientHandler.class);
    private ChannelHandlerContext context;
    private final MessageResponseCount[] counts = new MessageResponseCount[1024];
    private HostInfo hostInfo;


    public ClusterClientHandler(String ip, int port){
        this.hostInfo = new HostInfo(ip, port);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context = ctx;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Frame frame = (Frame) msg;
        int frameId = frame.getFrameId();
        //是移除消息的请求
        if (frame.getType() == Frame.PayloadType.REMOVED_MESSAGE_SUCCESSFULLY.getType()) {
            int index = frame.getFrameId() & (1024 - 1);
            MessageResponseCount count = counts[index];
            logger.info("响应:请求移除消息时frameId:" + frame);
            if(count != null){
                count.messageRemoved();
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.info("失去与Broker的连接");
        AmazingMqClusterApplication.getInstance().removeHandlerAndHostInfo(this, hostInfo);
    }

    public void send(Frame frame){
        context.writeAndFlush(frame);
    }

    public void sendRemoveMessage(MessageResponseCount count, Frame frame) throws InterruptedException {
        int index = frame.getFrameId() & (1024 - 1);
        logger.info("请求:请求移除消息时frameId:" + frame);
        counts[index] = count;
        context.writeAndFlush(frame);
    }

}
