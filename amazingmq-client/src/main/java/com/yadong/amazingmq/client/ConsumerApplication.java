package com.yadong.amazingmq.client;

import com.sun.xml.internal.messaging.saaj.soap.Envelope;
import com.yadong.amazingmq.client.channel.Channel;
import com.yadong.amazingmq.client.connection.Connection;
import com.yadong.amazingmq.client.connection.ConnectionFactory;
import com.yadong.amazingmq.client.consumer.Consumer;
import com.yadong.amazingmq.client.consumer.DefaultConsumer;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

public class ConsumerApplication {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerApplication.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setHost("127.0.0.1");
        factory.setVirtualHost("/");
        // 建立到AmazingMq的连接
        Connection connection = factory.newConnection();
        // 创建信道
        Channel channel = connection.createChannel();
//        // 声明交换机
//        channel.exchangeDeclare("hello-exchange","direct", false);
//        // 声明队列
//        channel.queueDeclare("hello-queue", false, false, false, null);
//        // 声明绑定
//        channel.queueBind("hello-queue", "hello-exchange", "binding-1");
        // 发布消息
        channel.basicConsume("hello-queue", false, "", new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, byte[] body) throws IOException {
                logger.info("接受到消息:" + new String(body));
            }
        });
    }
}
