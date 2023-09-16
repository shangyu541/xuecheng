/**
 * FileName: RabbitmqConfig
 * Author:   admin
 * Date:     2020/7/13 17:31
 * Description: rabbit工具雷
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.manage_cms.config;

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
    public static final String EX_ROUTING_CMS_POSTAGE="ex_routing_cms_postage";

    /**
     * 声明交换机配置
     * @return
     */
    @Bean(EX_ROUTING_CMS_POSTAGE)
    public Exchange EXCHANGE_FANNOT_INFO(){
        return ExchangeBuilder.directExchange(EX_ROUTING_CMS_POSTAGE).durable(true).build();
    }
}
