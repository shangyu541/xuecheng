package com.xucheng.govern.center; /**
 * FileName: com.xucheng.govern.center.GovernCenterApplication
 * Author:   admin
 * Date:     2020/7/16 17:16
 * Description: 启动了
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 〈一句话功能简述〉<br> 
 * 〈启动了〉
 * @author admin
 * @create 2020/7/16
 * @since 1.0.0
 */
@EnableEurekaServer
@SpringBootApplication
public class GovernCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(GovernCenterApplication.class,args);
    }
}
