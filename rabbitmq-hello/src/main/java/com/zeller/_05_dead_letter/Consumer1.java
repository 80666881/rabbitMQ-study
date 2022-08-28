package com.zeller._05_dead_letter;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.zeller.utils.RabbitMqUtils;

/**
 * 死信队列实战
 * 消费者1
 */
public class Consumer1 {
    //普通交换机
    private static final String NORMAL_EXCHANGE = "normal_exchange";
    //死信交换机
    private static final String DEAD_EXCHANGE = "dead_exchange";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
    }
}
