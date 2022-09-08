package com.yadong.amazingmq.client.netty;


import com.yadong.amazingmq.client.netty.handler.BrokerNettyClient;
import com.yadong.amazingmq.client.netty.handler.BrokerNettyClientHandler;
import com.yadong.amazingmq.codec.BrokerNettyDecoder;
import com.yadong.amazingmq.codec.BrokerNettyEncoder;
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
 * @date 2022/9/5 9:56
 * @Description 连接Broker
 */
public class AmazingMqNettyClient {



    private static final Logger logger = LoggerFactory.getLogger(AmazingMqNettyClient.class);

    // 外部使用这个方法来创建一个Connection连接AmazingMqBroker的NettyServer
    public static BrokerNettyClient createAndConnect(String hostname, int port) throws InterruptedException {
        return new AmazingMqNettyClient().createClient(hostname, port);
    }

    private AmazingMqNettyClient(){
    }

    public BrokerNettyClient createClient(String hostname, int port) throws InterruptedException {
        return createClient0(hostname, port);
    }

    private BrokerNettyClient createClient0(String hostname, int port) throws InterruptedException {

        BrokerNettyClientHandler client = new BrokerNettyClientHandler();
        NioEventLoopGroup group = new NioEventLoopGroup(8);
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
        logger.info("启动 Client 成功, 连接:[ " +hostname +":" + port +"]");
        return client;
    }


}
