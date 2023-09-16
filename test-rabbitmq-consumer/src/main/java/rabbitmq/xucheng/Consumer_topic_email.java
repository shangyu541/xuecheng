/**
 * FileName: Consumer_email
 * Author:   admin
 * Date:     2020/7/13 9:50
 * Description: 邮件消费者
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package rabbitmq.xucheng;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 〈一句话功能简述〉<br> 
 * 〈邮件消费者〉
 * @author admin
 * @create 2020/7/13
 * @since 1.0.0
 */
public class Consumer_topic_email {

    private static final String QUEUE_INFO_EMAIL="queue_topic_email";
    private static final String EXCHANGE_FANNOT_INFO="exchange_topic_info";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory=new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setPassword("guest");
        connectionFactory.setUsername("guest");
        connectionFactory.setVirtualHost("/");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        /**
         * 声明交换机，声明交换机类型
         */
        channel.exchangeDeclare(EXCHANGE_FANNOT_INFO,BuiltinExchangeType.TOPIC,true,false,false,null);
        /**
         * 声明队列 queue
         */
        channel.queueDeclare(QUEUE_INFO_EMAIL,true,false,false,null);
        /**
         * 限定只接收一次
         */
//        channel.basicQos(1);
        channel.queueBind(QUEUE_INFO_EMAIL,EXCHANGE_FANNOT_INFO,"info.#.email.#");
        DefaultConsumer defaultConsumer=new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String s =new String(body,"UTF-8");
                /**
                 * 手动确认，反馈给发送端
                 *
                 * 手动拒绝，消息会被丢弃，不会重回队列
                 *channel.basicReject(envelope.getDeliveryTag(),false);
                 */
                channel.basicAck(envelope.getDeliveryTag(),false);
                System.out.println("email:"+s);
            }
        };
        /**
         * 消费消息
         */
        channel.basicConsume(QUEUE_INFO_EMAIL,false,defaultConsumer);
    }
}
