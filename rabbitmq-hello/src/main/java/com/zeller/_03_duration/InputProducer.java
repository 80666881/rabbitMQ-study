package com.zeller._03_duration;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.zeller.utils.RabbitMqUtils;

import java.util.Scanner;

public class InputProducer {
    //    队列名称
    public static final String QUEUE_NAME = "durable_queue";

    //    发消息
    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();
        /**
         * 1.queueName
         * 2.队列是否持久化durable
         * 3.是否独占
         * 4.其他参数
         */
        //注意，队列持久化不能和旧的队列配置冲突，比如旧的队列是非持久化的，
        // 【1】要想持久化，只能删除或者新建别的队列,此时只是队列持久化，消息还是会丢失
        boolean isDurable = true;
        channel.queueDeclare(QUEUE_NAME, isDurable, false, false, null);
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()){
            String msg = scanner.next();
            /*
            * 【2】消息持久化
            * MessageProperties.PERSISTENT_TEXT_PLAIN,保存到磁盘
            * */
            channel.basicPublish("",QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,msg.getBytes("UTF-8"));
            System.out.println("生产者发送消息:"+msg);
        }
    }
}
