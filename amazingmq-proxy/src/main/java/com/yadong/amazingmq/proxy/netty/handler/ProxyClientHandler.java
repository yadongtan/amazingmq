package com.yadong.amazingmq.proxy.netty.handler;

import com.yadong.amazingmq.frame.Frame;
import com.yadong.amazingmq.property.HostInfo;
import com.yadong.amazingmq.proxy.AmazingProxy;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* @author YadongTan
* @date 2022/9/15 11:56
* @Description 集群间通信的client
*/
public class ProxyClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ProxyClientHandler.class);

    private ChannelHandlerContext context;
    private final ChannelHandlerContext acceptorContext;
    private MessageTransmitter transmitter;
    private HostInfo hostInfo;


    public ProxyClientHandler(MessageTransmitter transmitter, String hostname, int port, ChannelHandlerContext context) {
        this.transmitter = transmitter;
        hostInfo = new HostInfo(hostname, port);
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
        //发生异常, 关闭通道, 将该主机标记为不可用
        if (transmitter != null) {
            AmazingProxy.getInstance().getHostList().remove(hostInfo);
            logger.info("[" + hostInfo.getIp() + ":" + hostInfo.getPort() + "]不可用,将切换服务器");
            transmitter.reconnectClient();
        }
    }

    public void send(Frame frame){
        context.writeAndFlush(frame);
    }
}
