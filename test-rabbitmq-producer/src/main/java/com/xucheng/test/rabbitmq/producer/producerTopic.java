/**
 * FileName: producer02
 * Author:   admin
 * Date:     2020/7/10 16:39
 * Description: 发布订阅模式
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xucheng.test.rabbitmq.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 〈一句话功能简述〉<br> 
 * 〈路由模式〉
 * @author admin
 * @create 2020/7/10
 * @since 1.0.0
 */
public class producerTopic {
    /**
     * topic模式，不用对交换机和队列绑定
     *
     *   *.orange.* :  匹配以 任意一个单词字符开头中间包含 .orange. 以任意一个单词字符结尾 的字符串。比如 a.orange.b, sdfsd.orange.fdsfsdf 等（注意是一个单词）。
     *
     *   lay.# ：只要一lay.开头的都匹配，他可以匹配lay.a, lay.a.b, lay.b.c等。
     */


    private static final String QUEUE_INFO_EMAIL="queue_topic_email";
    private static final String QUEUE_INFO_SMS="queue_topic_sms";
    private static final String EXCHANGE_FANNOT_INFO="exchange_topic_info";

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory connectionFactory=new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setPassword("guest");
        connectionFactory.setUsername("guest");
        connectionFactory.setVirtualHost("/");
        Connection connection = connectionFactory.newConnection();
        /**
         * 创建一个消息连接
         */
        Channel channel = connection.createChannel();
        /**
         * 声明队列queue
         */
        channel.queueDeclare(QUEUE_INFO_SMS,true,false,false,null);
        channel.queueDeclare(QUEUE_INFO_EMAIL,true,false,false,null);
        /**
         * 声明交换机和交换机类型
         */
        channel.exchangeDeclare(EXCHANGE_FANNOT_INFO, BuiltinExchangeType.TOPIC,true,false,false,null);
        /**
         * 对交换机和队列进行绑定，设定一个routingKey,绑定路由
         */
        channel.queueBind(QUEUE_INFO_EMAIL,EXCHANGE_FANNOT_INFO,"info.#.email.#");
        channel.queueBind(QUEUE_INFO_SMS,EXCHANGE_FANNOT_INFO,"info.#.sms.#");
        /**
         * 发送消息到队列中
         */
        String s="发布订阅消息模式_sms";
        for (int i = 0; i < 5; i++) {
            /**
             * 对这个消息发送到专用路由上
             */
            channel.basicPublish(EXCHANGE_FANNOT_INFO, "info.sms",null,s.getBytes());
            System.out.println("测试发送:"+i);
        }
        String email="发布订阅消息模式_email";
        for (int j = 0; j < 5; j++) {
            /**
             * 对这个消息发送到专用路由上
             */
            channel.basicPublish(EXCHANGE_FANNOT_INFO, "info.email",null,email.getBytes());
            System.out.println("测试发送:"+j);
        }
        String email_sms="发布订阅消息模式_email_sms";
        for (int q = 0; q < 5; q++) {
            /**
             * 对这个消息发送到专用路由上
             */
            channel.basicPublish(EXCHANGE_FANNOT_INFO, "info.sms.email",null,email_sms.getBytes());
            System.out.println("测试发送:"+q);
        }
        channel.close();
        connection.close();
    }
}
