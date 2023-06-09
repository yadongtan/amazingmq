package com.yadong.amazingmq.client.channel;

import com.yadong.amazingmq.client.connection.Connection;
import com.yadong.amazingmq.client.consumer.Consumer;
import com.yadong.amazingmq.client.netty.handler.BrokerNettyClient;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface Channel {

    int getChannelNumber();

    Connection getConnection();

    boolean exchangeDeclare(String exchangeName, String mode, boolean duration) throws ExecutionException, InterruptedException;

    boolean queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments) throws ExecutionException, InterruptedException;

    boolean queueBind(String queueName, String exchangeName, String routingKey) throws ExecutionException, InterruptedException;

    boolean basicPublish(String exchangeName, String routingKey, Map<String, Object> basicProperties, byte[] messageBodyBytes) throws ExecutionException, InterruptedException;

    boolean basicConsume(String queueName, boolean autoAck, String consumerTag, Consumer consumer) throws ExecutionException, InterruptedException, IOException;

    boolean close() throws ExecutionException, InterruptedException;
}
