package com.yadong.amazingmq.client;


import com.yadong.amazingmq.client.connection.Connection;
import com.yadong.amazingmq.client.connection.ConnectionFactory;

import java.util.concurrent.ExecutionException;

public class ClientApplication {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setHost("127.0.0.1");
        factory.setVirtualHost("/");
        // 建立到代理服务器的连接
        Connection connection = factory.newConnection();
    }
}
