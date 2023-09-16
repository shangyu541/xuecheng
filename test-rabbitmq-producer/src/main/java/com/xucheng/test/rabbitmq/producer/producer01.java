/**
 * FileName: producer
 * Author:   admin
 * Date:     2020/7/3 13:45
 * Description: 生产者
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xucheng.test.rabbitmq.producer;

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
 * 〈生产者〉
 * @author admin
 * @create 2020/7/3
 * @since 1.0.0
 */
public class producer01 {

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
        channel.queueDeclare("7.3test",true,false,false,null);
        /**
         * 发送消息到队列种
         */
        channel.basicPublish("","7.3test",null,"helloword".getBytes());
        channel.close();
        connection.close();
    }
}
