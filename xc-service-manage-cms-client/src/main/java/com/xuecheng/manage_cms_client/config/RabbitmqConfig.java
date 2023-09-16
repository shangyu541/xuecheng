/**
 * FileName: RabbitmqConfig
 * Author:   admin
 * Date:     2020/7/13 17:31
 * Description: rabbit工具雷
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.manage_cms_client.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 〈一句话功能简述〉<br> 
 * 〈rabbit工具类〉
 * @author admin
 * @create 2020/7/13
 * @since 1.0.0
 */
@Configuration
public class RabbitmqConfig {
    public static final String QUEUE_CMS_POSTPAGE="queue_cms_postpage";
    public static final String EX_ROUTING_CMS_POSTAGE="ex_routing_cms_postage";

    @Value("${xuecheng.mq.queue}")
    private String QUEUE_CMS_POSTPAGE_NAME;

    @Value("${xuecheng.mq.routingKey}")
    private String ROUTING_KEY;

    /**
     * 声明交换机配置
     * @return
     */
    @Bean(EX_ROUTING_CMS_POSTAGE)
    public Exchange EXCHANGE_FANNOT_INFO(){
        return ExchangeBuilder.directExchange(EX_ROUTING_CMS_POSTAGE).durable(true).build();
    }

    /**
     * 声明队列
     * @return
     */
    @Bean(QUEUE_CMS_POSTPAGE)
    public Queue QUEUE_INFO_SMS(){
        return new Queue(QUEUE_CMS_POSTPAGE_NAME);
    }


    /**
     * 绑定队列到交换机
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding BIND_QUEUE_INFO_SMS(@Qualifier(QUEUE_CMS_POSTPAGE) Queue queue, @Qualifier(EX_ROUTING_CMS_POSTAGE) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY).noargs();
    }

}
