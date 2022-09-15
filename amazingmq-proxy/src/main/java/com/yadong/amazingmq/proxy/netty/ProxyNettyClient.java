package com.yadong.amazingmq.proxy.netty;


import com.yadong.amazingmq.codec.BrokerNettyDecoder;
import com.yadong.amazingmq.codec.BrokerNettyEncoder;
import com.yadong.amazingmq.property.HostInfo;
import com.yadong.amazingmq.proxy.netty.handler.ProxyClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* @author YadongTan
* @date 2022/9/14 14:39
* @Description 提供集群之间交互通讯的client
*/
public class ProxyNettyClient {


    private static final Logger logger = LoggerFactory.getLogger(ProxyNettyClient.class);

    // 外部使用这个方法来创建一个Connection连接AmazingMqBroker的NettyServer
    public static ProxyClientHandler createAndConnect(ChannelHandlerContext context, HostInfo hostInfo) throws InterruptedException {
        return createAndConnect(context, hostInfo.getIp(), hostInfo.getPort());
    }

    public static ProxyClientHandler createAndConnect(ChannelHandlerContext context, String hostname, int port) throws InterruptedException {
        return new ProxyNettyClient().createClient(context, hostname, port);
    }

    private ProxyNettyClient(){
    }

    public ProxyClientHandler createClient(ChannelHandlerContext context, String hostname, int port) throws InterruptedException {
        return createClient0(context, hostname, port);
    }

    private ProxyClientHandler createClient0(ChannelHandlerContext context, String hostname, int port) throws InterruptedException {

        ProxyClientHandler client = new ProxyClientHandler(context);
        NioEventLoopGroup group = new NioEventLoopGroup(1);
        Bootstrap bootstrap = new Bootstrap();
        ByteBuf delimiter = Unpooled.copiedBuffer(BrokerNettyEncoder.DELIMITER.getBytes());
        bootstrap.group(group)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline
                                //.addLast(new LineBasedFrameDecoder(1024))
                                .addFirst(new DelimiterBasedFrameDecoder(60000,delimiter))
                                .addLast(new BrokerNettyDecoder())
                                .addLast(new BrokerNettyEncoder())
                                .addLast(client);
                    }
                });
        ChannelFuture channelFuture = bootstrap.connect(hostname, port).sync();
        Channel channel = channelFuture.channel();
        logger.info("启动 ClusterClient 成功, 连接:[ " +hostname +":" + port +"]");
        return client;
    }

}
