package com.zeller;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.zeller.utils.RabbitMqUtils;

public class Producer {
    //    队列名称
    public static final String QUEUE_NAME = "hello";

    //    发消息
    public static void main(String[] args) throws Exception {
// //    创建一个连接工程
//         ConnectionFactory factory = new ConnectionFactory();
// //    ip，连接rabbitMQ的队列
//         factory.setHost("127.0.0.1");
//         factory.setUsername("admin");
//         factory.setPassword("123");
//
//         // 创建连接
//         Connection connection = factory.newConnection();
// //    获取信道
//         Channel channel = connection.createChannel();
        Channel channel = RabbitMqUtils.getChannel();

//         /**
//          * 生成队列
//          * 1.队列名称
//          * 2.队列里面的消息是否持久化
//          * 3.是否只供一个消费者进行消费
//          * 4.是否自动删除
//          */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String msg = "helloword-zzh";
        /**
         * 1.发动到哪个交换机
         * 2.路由key是哪个
         * 3.其他参数信息
         */
        channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
    }
}
