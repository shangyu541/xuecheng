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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 〈一句话功能简述〉<br> 
 * 〈发布订阅模式〉
 * @author admin
 * @create 2020/7/10
 * @since 1.0.0
 */
public class producerPublic {

    private static final String QUEUE_INFO_EMAIL="queue_info_email";
    private static final String QUEUE_INFO_SMS="queue_info_sms";
    private static final String EXCHANGE_FANNOT_INFO="exchange_fannot_info";

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
        channel.exchangeDeclare(EXCHANGE_FANNOT_INFO, BuiltinExchangeType.FANOUT,true,false,false,null);
        /**
         * 对交换机和队列进行绑定
         */
        channel.queueBind(QUEUE_INFO_EMAIL,EXCHANGE_FANNOT_INFO,"");
        channel.queueBind(QUEUE_INFO_SMS,EXCHANGE_FANNOT_INFO,"");
        /**
         * 发送消息到队列中
         */
        String s="发布订阅消息模式";
        for (int i = 0; i < 5; i++) {
            channel.basicPublish(EXCHANGE_FANNOT_INFO, "",null,s.getBytes());
            System.out.println("测试发送:"+i);
        }
        channel.close();
        connection.close();
    }
}
