package com.zeller.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMqUtils {

    public static Channel getChannel() throws Exception {
        // 创建一个连接工程
        ConnectionFactory factory = new ConnectionFactory();
//    ip，连接rabbitMQ的队列
        factory.setHost("127.0.0.1");
        factory.setUsername("admin");
        factory.setPassword("123");

        // 创建连接
        Connection connection = factory.newConnection();
        //    获取信道
        Channel channel = connection.createChannel();
        /**
         * 生成队列
         * 1.队列名称
         * 2.队列里面的消息是否持久化
         * 3.是否只供一个消费者进行消费
         * 4.是否自动删除
         */
        // channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        return channel;
    }

}
