/**
 * FileName: AuthService
 * Author:   admin
 * Date:     2020/8/12 10:43
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.auth.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.client.XcServiceList;
import com.xuecheng.framework.domain.ucenter.ext.AuthToken;
import com.xuecheng.framework.domain.ucenter.response.AuthCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.smartcardio.CommandAPDU;
import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 * @author admin
 * @create 2020/8/12
 * @since 1.0.0
 */
@Service
public class AuthService {

    @Value("${auth.tokenValiditySeconds}")
    int tokenValiditySeconds;

    @Autowired
    LoadBalancerClient loadBalancerClient;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RestTemplate restTemplate;


    /**
     * 流程，就是先去访问令牌，然后把access_token和jti换个位置，然后将令牌放到redis中使用
     * @param password
     * @param username
     * @param clientId
     * @param clientSecret
     * @return
     */
    //用户认证申请令牌
    public AuthToken login(String password, String username, String clientId, String clientSecret) {
        //请求spring security申请令牌
        AuthToken authToken=this.applyToken(username,password,clientId,clientSecret);
        if (authToken==null){
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_ERROR);
        }
        //将tonken 存入redis
        String access_token = authToken.getAccess_token();
        String string = JSON.toJSONString(authToken);
        Boolean saveTokenResult=saveToken(access_token,string,tokenValiditySeconds);
        if (!saveTokenResult){
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_TOKEN_SAVEFAIL);
        }
        return authToken;
    }

    private Boolean saveToken(String access_token, String content, int tokenValiditySeconds) {
        String name="user_token:"+access_token;
        //存入到redis中
        redisTemplate.boundValueOps(name).set(content,tokenValiditySeconds, TimeUnit.SECONDS);
        //获取过期时间
        Long expire = redisTemplate.getExpire(name);
        return expire>0;
    }

    /**
     * 流程：通过用户信息和密钥，对密钥进行httbasic认证，然后去访问获取令牌，返回authToken
     * @param username
     * @param password
     * @param clientId
     * @param clientSecret
     * @return
     */
    private AuthToken applyToken(String username, String password, String clientId, String clientSecret) {
        ServiceInstance choose = loadBalancerClient.choose(XcServiceList.XC_SERVICE_UCENTER_AUTH);
        if (choose==null){
            ExceptionCast.cast(CommonCode.SERVER_ERROR);
            return null;
        }
        URI uri = choose.getUri();
        String authUrl=uri+"/auth/oauth/token";
        //请求内容分两部分，一部分是header 一部分是内容
        //header信息，包含了http basic信息
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        String httpbasic=httpbasic(clientId,clientSecret);
        headers.add("Authorization",httpbasic);
        // 包括grant_type username password
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type","password");
        body.add("username",username);
        body.add("password",password);
        HttpEntity<MultiValueMap<String, String>> multiValueMapHttpEntity = new HttpEntity<MultiValueMap<String, String>>(body,headers);
        //指定restTemplate当遇到400 或者401 的时候也不抛出异常
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getRawStatusCode()!=400 && response.getRawStatusCode()!=401){
                    super.handleError(response);
                }
            }
        });
        //远程调用令牌
        /**
         * url 请求地址
         * method  请求类型
         * requestEntity  请求内容
         * responseType 返回数据类型
         */
        ResponseEntity<Map> exchange = restTemplate.exchange(authUrl, HttpMethod.POST, multiValueMapHttpEntity, Map.class);
        Map map = exchange.getBody();
        if (map == null ||  map.get("access_token")==null || map.get("refresh_token")==null || map.get("jti")==null){
            if (map!=null && map.get("error_description")!=null){
                String error_description = (String) map.get("error_description");
                if (error_description.indexOf("UserDetailsService returned null")>=0){
                    ExceptionCast.cast(AuthCode.AUTH_ACCOUNT_NOTEXISTS);
                }else if (error_description.indexOf("坏的凭证")>=0){
                    ExceptionCast.cast(AuthCode.AUTH_CREDENTIAL_ERROR);
                }
            }
            return null;
        }
        AuthToken authToken = new AuthToken();
        //访问令牌
        String access_token = (String) map.get("jti");
        //刷新令牌
        String refresh_token = (String) map.get("refresh_token");
        //jti用户标识
        String jti = (String) map.get("access_token");
        authToken.setAccess_token(access_token);
        authToken.setRefresh_token(refresh_token);
        authToken.setJwt_token(jti);
        return authToken;
    }

    private String httpbasic(String clientId, String clientSecert) {
        //添加basic认证
        String s=clientId+":"+clientSecert;
        byte[] encode = Base64Utils.encode(s.getBytes());
        return "Basic "+new String(encode);
    }

    public boolean deleteToken(String cookieByJwt) {
        String key="user_token:"+cookieByJwt;
        redisTemplate.delete(key);
        Long expire = redisTemplate.getExpire(key, TimeUnit.SECONDS);
        return expire<0;
    }
}
