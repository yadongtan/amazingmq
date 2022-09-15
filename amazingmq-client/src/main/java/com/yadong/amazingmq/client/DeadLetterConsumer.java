package com.yadong.amazingmq.client;

import com.yadong.amazingmq.client.channel.Channel;
import com.yadong.amazingmq.client.connection.Connection;
import com.yadong.amazingmq.client.connection.ConnectionFactory;
import com.yadong.amazingmq.client.consumer.DefaultConsumer;
import com.yadong.amazingmq.frame.Envelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class DeadLetterConsumer {

    private static final Logger logger = LoggerFactory.getLogger(DeadLetterConsumer.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setHost("127.0.0.1");
        //factory.setPort(36666);
        factory.setVirtualHost("/");
        // 建立到AmazingMq的连接
        Connection connection = factory.newConnection();
        // 创建信道
        Channel channel = connection.createChannel();
        // 接收消息
        while(true){
            channel.basicConsume("dead-queue-1", false, "", new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, byte[] body) throws IOException {
                    logger.info("接受到消息:" + new String(body));
                }
            });
        }

    }
}
