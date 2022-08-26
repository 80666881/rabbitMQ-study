package com.zeller;

import com.rabbitmq.client.*;

public class Consumer {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception{
    //    创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setUsername("admin");
        factory.setPassword("123");

    //    建立连接
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

    //  接收消息  声明
        DeliverCallback deliverCallback = (consumerTag,message)->{
            System.out.println(new String(message.getBody()));
        };
        //取消消息
        CancelCallback cancelCallback = (consumerTag)->{
            System.out.println("消息消费被中断");
        };

    //    消费消息
        /*
        * 1.队列名称
        * 2.消费成功是否要自动应答
        * 3.消费者未成功消费的回调
        * 4.消费者取消消费的回调
        * */
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
