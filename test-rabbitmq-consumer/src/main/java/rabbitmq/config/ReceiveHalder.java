/**
 * FileName: RabbitMqConfig
 * Author:   admin
 * Date:     2020/7/10 16:31
 * Description: rabbitmq工具雷
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package rabbitmq.config;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


/**
 * 〈一句话功能简述〉<br> 
 * 〈rabbitmq工具类〉
 * @author admin
 * @create 2020/7/10
 * @since 1.0.0
 */
@Component
public class ReceiveHalder {
    @RabbitListener(queues = {RabbitMqConfig.QUEUE_INFO_EMAIL})
    public void receive_email(String msg, Message message, Channel channel){
        System.out.println(msg);
    }

    @RabbitListener(queues = {RabbitMqConfig.QUEUE_INFO_SMS})
    public void receive_sms(String msg, Message message, Channel channel){
        System.out.println(msg);
    }

}
