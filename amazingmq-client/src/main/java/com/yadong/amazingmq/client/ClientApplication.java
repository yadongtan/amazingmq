package com.yadong.amazingmq.client;


import com.yadong.amazingmq.client.channel.Channel;
import com.yadong.amazingmq.client.connection.Connection;
import com.yadong.amazingmq.client.connection.ConnectionFactory;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

public class ClientApplication {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setHost("127.0.0.1");
        factory.setVirtualHost("/");
        // 建立到AmazingMq的连接
        Connection connection = factory.newConnection();
        // 创建信道
        Channel channel = connection.createChannel();
        // 声明交换机
        channel.exchangeDeclare("hello-exchange","direct", false);
        // 声明队列
        channel.queueDeclare("hello-queue", false, false, false, null);
        // 声明绑定
        channel.queueBind("hello-queue", "hello-exchange", "binding-1");
        // 发布消息
        channel.basicPublish("hello-exchange","binding-1", null, "hello world!".getBytes(StandardCharsets.UTF_8));
    }
}
