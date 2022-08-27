package com.zeller._02_ackMsg;

import com.rabbitmq.client.Channel;
import com.zeller.utils.RabbitMqUtils;

import java.util.Scanner;

public class InputProducer {
    //    队列名称
    public static final String QUEUE_NAME = "ack_queue";

    //    发消息
    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();
        /**
         * 1.queueName
         * 2.是否持久化durable
         * 3.是否独占
         * 4.其他参数
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()){
            String msg = scanner.next();
            channel.basicPublish("",QUEUE_NAME,null,msg.getBytes("UTF-8"));
            System.out.println("生产者发送消息:"+msg);
        }
    }
}
