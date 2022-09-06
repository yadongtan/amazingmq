package com.yadong.amazingmq.server.netty;

import com.yadong.amazingmq.codec.BrokerNettyDecoder;
import com.yadong.amazingmq.codec.BrokerNettyEncoder;
import com.yadong.amazingmq.server.netty.handler.BrokerNettyHandler;
import com.yadong.amazingmq.server.property.BrokerProperties;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
* @author YadongTan
* @date 2022/9/4 23:00
* @Description 暴露Broker提供的NettyServer, 单例懒汉式
*/
public class BrokerNettyServer {

    private static final Logger logger = LoggerFactory.getLogger(BrokerNettyServer.class);

    private BrokerNettyServer(){}

    private static BrokerNettyServer _INSTANCE;

    public static BrokerNettyServer getInstance() {
        if(_INSTANCE == null){
            synchronized (BrokerNettyServer.class){
                if(_INSTANCE == null){
                    _INSTANCE = new BrokerNettyServer();
                }
            }
        }
        return _INSTANCE;
    }

    // the really method to start provider server
    public void syncStart(BrokerProperties properties){
        new Thread(()->{
            start(properties);
        }).start();
    }


    // the really method to start provider server
    private void start(BrokerProperties properties){
        int port = properties.getPort();
        try {
            String hostAddress = properties.getHost();
            if(hostAddress == null){
                hostAddress = InetAddress.getLocalHost().getHostAddress();
            }
            start0(hostAddress, port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private void start0(String hostname, int port){
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(64);
        ServerBootstrap bootstrap = new ServerBootstrap();
        ByteBuf delimiter = Unpooled.copiedBuffer(BrokerNettyEncoder.DELIMITER.getBytes());
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline
                                //.addLast(new LineBasedFrameDecoder(1024))
                                .addFirst(new DelimiterBasedFrameDecoder(60000,delimiter))
                                .addLast(new BrokerNettyDecoder())
                                .addLast(new BrokerNettyEncoder())
                                //.addLast(new LineBasedFrameDecoder(1024))
                                .addLast(new BrokerNettyHandler());
                    }
                });
        try {
            ChannelFuture channelFuture = bootstrap.bind(hostname, port);
            Channel channel = channelFuture.channel();
            logger.info("启动 Broker [" + hostname + ":" + port + "] 成功");
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
