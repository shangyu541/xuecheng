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

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 〈一句话功能简述〉<br> 
 * 〈rabbitmq工具类〉
 * @author admin
 * @create 2020/7/10
 * @since 1.0.0
 */
@Configuration
public class RabbitMqConfig {
    public static final String QUEUE_INFO_EMAIL="queue_topic_email";
    public static final String QUEUE_INFO_SMS="queue_topic_sms";
    public static final String EXCHANGE_FANNOT_INFO="exchange_topic_info";

    /**
     * 声明交换机配置
     * @return
     */
    @Bean(EXCHANGE_FANNOT_INFO)
    public Exchange EXCHANGE_FANNOT_INFO(){
        return ExchangeBuilder.topicExchange(EXCHANGE_FANNOT_INFO).durable(true).build();
    }

    /**
     * 声明队列
     * @return
     */
    @Bean(QUEUE_INFO_SMS)
    public Queue QUEUE_INFO_SMS(){
        return new Queue(QUEUE_INFO_SMS);
    }

    /**
     * 声明队列
     * @return
     */
    @Bean(QUEUE_INFO_EMAIL)
    public Queue QUEUE_INFO_EMAIL(){
        return new Queue(QUEUE_INFO_EMAIL);
    }

    /**
     * 绑定队列到交换机
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding BIND_QUEUE_INFO_SMS(@Qualifier(QUEUE_INFO_SMS) Queue queue, @Qualifier(EXCHANGE_FANNOT_INFO) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("info.#.sms.#").noargs();
    }

    /**
     * 绑定队列到交换机
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding BIND_QUEUE_INFO_EMAIL(@Qualifier(QUEUE_INFO_EMAIL) Queue queue, @Qualifier(EXCHANGE_FANNOT_INFO) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("info.#.sms.#").noargs();
    }


}
