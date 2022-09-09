# amazingmq

#### 介绍
AmazingMq, 一款基于自定义协议的高吞吐量消息队列中间件


软件架构说明

![输入图片说明](https://foruda.gitee.com/images/1662707423638079956/2a35451c_8370887.png "AmazingMq架构设计图 (5).png")

#### 使用说明


启动AmazingMq的Main方法:

```

    public static void main(String[] args) {
        AmazingMqBroker.getInstance().start();
        if (ENABLE_WEB) {
            SpringApplication.run(BrokerApplication.class, args);
        }
    }

```





生产者测试代码:

```
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

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()){
            String msg = scanner.nextLine();
            channel.basicPublish("hello-exchange","binding-1", null, msg.getBytes(StandardCharsets.UTF_8));
        }

```

消费者代码:

```

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setHost("127.0.0.1");
        factory.setVirtualHost("/");
        // 建立到AmazingMq的连接
        Connection connection = factory.newConnection();
        // 创建信道
        Channel channel = connection.createChannel();
        // 声明交换机, 如果已经有了不会重复创建的
        channel.exchangeDeclare("hello-exchange","direct", false);
        // 声明队列, 如果已经有了不会重复创建的
        channel.queueDeclare("hello-queue", false, false, false, null);
        // 声明绑定, 如果已经有了不会重复创建的
        channel.queueBind("hello-queue", "hello-exchange", "binding-1");
        // 发布消息
        while(true){
            channel.basicConsume("hello-queue", false, "", new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, byte[] body) throws IOException {
                    logger.info("接受到消息:" + new String(body));
                }
            });
        }

```

测试结果:


生产者:

![输入图片说明](https://foruda.gitee.com/images/1662707655310820923/835b05ab_8370887.png "生产者测试.png")


消费者:
![输入图片说明](https://foruda.gitee.com/images/1662707678809755960/29ae0217_8370887.png "消费者测试.png")



#### 参与贡献


ALL: [YadongTan]
