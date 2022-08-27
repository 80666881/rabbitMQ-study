package com.zeller._03_duration.confirmMsg;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.zeller.utils.RabbitMqUtils;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class ConfirmMsg {
    //批量发消息的个数

    public static final int MSG_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        /**
         * 1.单个确认
         * 2.批量确认
         * 3.异步批量确认
         */
        //ConfirmMsg.publishSingle();
        //ConfirmMsg.publishMultiple();
        ConfirmMsg.publishMessageAsync();
    }

    //单个确认
    public static void publishSingle() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, false, false, false, null);
        // 开启发布确认
        channel.confirmSelect();
        // 开始的时间
        long begin = System.currentTimeMillis();

        // 批量发消息
        for (int i = 0; i < MSG_COUNT; i++) {
            String msg = "~~~~~第" + i + "条消息~~~";
            channel.basicPublish("", queueName, null, msg.getBytes());
            boolean flag = channel.waitForConfirms();
            if (flag) {
                System.out.println("消息" + i + "发送——成功");
            }
        }
        long end = System.currentTimeMillis();

        System.out.println("发布" + MSG_COUNT + "个消息，消耗" + (end - begin) + "ms");


    }

    //批量同步发送确认
    public static void publishMultiple() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, false, false, false, null);
        // 开启发布确认
        channel.confirmSelect();
        // 开始的时间
        long begin = System.currentTimeMillis();

        // 批量确认消息大小
        int batchSize = 100;

        // 批量发消息
        for (int i = 0; i < MSG_COUNT; i++) {
            String msg = "~~~~~第" + i + "条消息~~~";
            channel.basicPublish("", queueName, null, msg.getBytes());
            boolean flag = channel.waitForConfirms();
            if ((i + 1) % batchSize == 0) {
                System.out.println("消息" + i + "发送——成功");
            }
        }
        long end = System.currentTimeMillis();

        System.out.println("发布" + MSG_COUNT + "个消息，消耗" + (end - begin) + "ms");


    }

    public static void publishMessageAsync() throws Exception {
        try (Channel channel = RabbitMqUtils.getChannel()) {
            String queueName = UUID.randomUUID().toString();
            channel.queueDeclare(queueName, false, false, false, null); //开启发布确认
            channel.confirmSelect();
            /**
             * 线程安全有序的一个哈希表，适用于高并发的情况
             * 1.轻松的将序号与消息进行关联
             * 2.轻松批量删除条目 只要给到序列号
             * 3.支持并发访问
             */
            ConcurrentSkipListMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();
            /**
             * 确认收到消息的一个回调
             * 1.消息序列号
             * 2.true 可以确认小于等于当前序列号的消息 * false确认当前序列号消息
             */
            ConfirmCallback ackCallback = (sequenceNumber, multiple) -> {
                if (multiple) {
                    //返回的是小于等于当前序列号的未确认消息 是一个map
                    ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(sequenceNumber, true);
                    //清除该部分未确认消息
                    confirmed.clear();
                } else {
                    //只清除当前序列号的消息
                    outstandingConfirms.remove(sequenceNumber);
                }
            };
            ConfirmCallback nackCallback = (sequenceNumber, multiple) -> {
                String message = outstandingConfirms.get(sequenceNumber);
                System.out.println("发布的消息" + message + "未被确认，序列号" + sequenceNumber);
            };
            /**
             * 添加一个异步确认的监听器
             * 1.确认收到消息的回调
             * 2.未收到消息的回调
             */
            channel.addConfirmListener(ackCallback, nackCallback);
            long begin = System.currentTimeMillis();
            for (int i = 0; i < MSG_COUNT; i++) {
                String message = "消息" + i;
                /**
                 * channel.getNextPublishSeqNo()获取下一个消息的序列号 * 通过序列号与消息体进行一个关联
                 * 全部都是未确认的消息体
                 */
                outstandingConfirms.put(channel.getNextPublishSeqNo(), message);
                channel.basicPublish("", queueName, null, message.getBytes());
            }
            long end = System.currentTimeMillis();
            System.out.println("发布" + MSG_COUNT + "个异步确认消息,耗时" + (end - begin) + "ms");
        }
    }
}
