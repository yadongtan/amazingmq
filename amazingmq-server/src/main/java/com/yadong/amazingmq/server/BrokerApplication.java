package com.yadong.amazingmq.server;

import com.yadong.amazingmq.server.netty.BrokerNettyServer;
import com.yadong.amazingmq.server.property.BrokerProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BrokerApplication {
    public static void main(String[] args) {
        SpringApplication.run(BrokerApplication.class);
    }

    @Bean
    public BrokerNettyServer brokerNettyServer(){
        BrokerNettyServer.getInstance().start(new BrokerProperties("localhost", 7000));
        return BrokerNettyServer.getInstance();
    }

}
