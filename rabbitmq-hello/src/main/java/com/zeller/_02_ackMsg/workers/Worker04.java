package com.zeller._02_ackMsg.workers;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.zeller.utils.RabbitMqUtils;
import com.zeller.utils.SleepUtils;


public class Worker04 {
    public static final String QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //  接收消息  声明
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            //沉睡一秒
            SleepUtils.sleep(1);
            System.out.println("worker-01接收到的消息" + new String(message.getBody()));
            /**
             * 1.消息tag
             * 2.是否批量处理
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };
        //取消消息
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消息消费被中断");
        };
        System.out.println("C1等待接收消息...");
        boolean autoAck = false;
        channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, cancelCallback);
    }
}
