package com.yadong.amazingmq.client;


import com.yadong.amazingmq.client.channel.Channel;
import com.yadong.amazingmq.client.connection.Connection;
import com.yadong.amazingmq.client.connection.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class ClientApplication {

    private static final Logger logger = LoggerFactory.getLogger(ClientApplication.class);

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
        channel.exchangeDeclare("hello-exchange-1","topic", false);
//        // 声明队列
//        channel.queueDeclare("hello-queue-1", false, false, false, null);
//        // 声明绑定
//        channel.queueBind("hello-queue-1", "hello-exchange-1", "aaa.*.ccc");
//        // 发布消息

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String msg = scanner.nextLine();
            channel.basicPublish("hello-exchange-1", "aaa.bbb.ccc", null, msg.getBytes(StandardCharsets.UTF_8));
        }
//        logger.info("压力测试开始...");
//        Connection[] connections = new Connection[60];
//        for (int i = 0; i < 1; i++) {
//            Connection connection1 = factory.newConnection();
//            connections[i] = connection1;
//        }
//
//
//        for(int i = 0;i < 1;i++){
//            int finalI = i;
//            new Thread(
//                    ()->{
//                        try {
//                            while(true){
//                                Channel channel1 = connections[finalI].createChannel();
//                                channel1.basicPublish("hello-exchange", "binding-1", null, "hello world".getBytes(StandardCharsets.UTF_8));
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//            ).start();
//        }
//        logger.info("压力测试结束...");
    }
}
