/**
 * FileName: ResourseServerConfig
 * Author:   admin
 * Date:     2020/8/7 15:22
 * Description: 客户端登陆验证类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.manage_course.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * 〈一句话功能简述〉<br> 
 * 〈客户端登陆验证类〉
 * @author admin
 * @create 2020/8/7
 * @since 1.0.0
 */
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true)//激活方法上的注解
public class ResourseServerConfig extends ResourceServerConfigurerAdapter {
    //公钥
    private static final String PUBLIC_KEY = "publickey.txt";

    //定义jwttkenstore 使用jwt令牌
    @Bean
    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter){
        return new JwtTokenStore(jwtAccessTokenConverter);
    }

    //定义JwtAccessTokenConverter 使用jwt令牌
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setVerifierKey(getPubKey());
        return converter;
    }

    /**
     * 获取非对称加密令牌
     * @return
     */
    private String getPubKey() {
        //获取文件信息
        ClassPathResource resource = new ClassPathResource(PUBLIC_KEY);
        try {
            //读取文件流信息
            InputStreamReader reader = new InputStreamReader(resource.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(reader);
            //将文件进行换行输出
            return bufferedReader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        //所有请求必须认证通过
        http.authorizeRequests()
                .antMatchers("/v2/api‐docs",
                        "/swagger‐resources/configuration/ui", "/swagger‐resources",
                        "/swagger‐resources/configuration/security", "/swagger‐ui.html",
                        "/webjars/**","/course/find/**").permitAll()
                .anyRequest().authenticated();
    }
}
