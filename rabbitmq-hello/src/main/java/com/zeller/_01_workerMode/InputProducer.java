package com.zeller._01_workerMode;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.zeller.utils.RabbitMqUtils;

import java.util.Scanner;

public class InputProducer {
    //    队列名称
    public static final String QUEUE_NAME = "hello";

    //    发消息
    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()){
            String msg = scanner.next();
            channel.basicPublish("",QUEUE_NAME,null,msg.getBytes());
            System.out.println("发送消息完成"+msg);
        }
    }
}
