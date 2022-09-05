package com.yadong.amazingmq.client;

import com.yadong.amazingmq.client.netty.AmazingMqNettyClient;

public class ClientApplication {

    public static void main(String[] args) throws InterruptedException {

        AmazingMqNettyClient client = new AmazingMqNettyClient();
        client.start("127.0.0.1",7000);

    }
}
