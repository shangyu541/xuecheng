package com.xuecheng.manage_course;

import com.xuecheng.framework.interceptor.FeignClientInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.client.RestTemplate;

/**
 * @author Administrator
 * @version 1.0  课程管理项目
 **/
@EnableFeignClients //开启feign服务
@EnableDiscoveryClient
@SpringBootApplication
@EntityScan("com.xuecheng.framework.domain.course")//扫描实体类
@ComponentScan(basePackages={"com.xuecheng.api"})//扫描接口
@ComponentScan(basePackages={"com.xuecheng.manage_course"})
@ComponentScan(basePackages={"com.xuecheng.framework"})//扫描common下的所有类
public class ManageCourseApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(ManageCourseApplication.class, args);
    }

//    @Bean
//    @LoadBalanced //开启客户端负载均衡功能
//    public RestTemplate restTemplate(){
//        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
//    }
//    //开启feign拦截器的bean
//    @Bean
//    public FeignClientInterceptor feignClientInterceptor(){
//        return new FeignClientInterceptor();
//    }
}
