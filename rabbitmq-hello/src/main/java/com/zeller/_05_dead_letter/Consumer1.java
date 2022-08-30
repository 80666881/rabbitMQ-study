package com.zeller._05_dead_letter;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.zeller.utils.RabbitMqUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 死信队列实战
 * 消费者1
 */
public class Consumer1 {
    //普通交换机
    private static final String NORMAL_EXCHANGE = "normal_exchange";
    //普通队列
    private static final String NORMAL_QUEUE = "normal_queue";
    //死信交换机
    private static final String DEAD_EXCHANGE = "dead_exchange";
    //死信队列
    private static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //声明交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
        //声明队列
        Map<String, Object> arguments = new HashMap<>();
        // 过期时间,10s = 10000ms，也可以由生产者指定
        //arguments.put("x-message-ttl",10000);
        // 正常队列过期之后的死信交换机
        arguments.put("x-dead-letter-exchange",DEAD_EXCHANGE);
        // 设置死信RoutingKey
        arguments.put("x-dead-letter-routing-key","lisi");
        channel.queueDeclare(NORMAL_QUEUE,false,false,false,arguments);
        channel.queueDeclare(DEAD_QUEUE,false,false,false,null);
        //绑定普通交换机->普通队列
        channel.queueBind(NORMAL_QUEUE,NORMAL_EXCHANGE,"zhangsan");
        //绑定死信交换机->死信队列
        channel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE,"lisi");

        System.out.println("C1等待接收消息....");



        DeliverCallback deliverCallback = (customTag,message)->{
            System.out.println("consumer1接受的消息是"+new String(message.getBody()));
        };

        channel.basicConsume(NORMAL_QUEUE,true,deliverCallback,(consumerTag -> {}));

    }
}
