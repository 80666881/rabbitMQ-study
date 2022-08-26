package com.zeller._01_workerMode.workers;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.zeller.utils.RabbitMqUtils;


public class Worker01 {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //  接收消息  声明
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("接收到的消息" + new String(message.getBody()));
            /**
             * 1.消息tag
             * 2.是否批量处理
             */
        };
        //取消消息
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消息消费被中断");
        };
        System.out.println("C1等待接收消息...");
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
