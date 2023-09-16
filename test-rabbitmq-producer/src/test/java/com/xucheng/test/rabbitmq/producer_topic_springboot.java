/**
 * FileName: producer_topic_springboot
 * Author:   admin
 * Date:     2020/7/13 15:41
 * Description: springboot整合测试类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xucheng.test.rabbitmq;

import com.xucheng.test.rabbitmq.config.RabbitMqConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 〈一句话功能简述〉<br> 
 * 〈springboot整合测试类〉
 * @author admin
 * @create 2020/7/13
 * @since 1.0.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class producer_topic_springboot {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSendByTopic(){
        for (int i = 0; i < 5 ; i++) {
            String message="测试输入："+i;
            rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_FANNOT_INFO,"info.sms.email",message);
            System.out.println(i);
        }
    }
}
