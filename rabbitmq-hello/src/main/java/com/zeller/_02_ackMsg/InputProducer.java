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

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()){
            String msg = scanner.next();
            channel.basicPublish("",QUEUE_NAME,null,msg.getBytes("UTF-8"));
            System.out.println("生产者发送消息:"+msg);
        }
    }
}
