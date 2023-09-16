/**
 * FileName: TestClient
 * Author:   admin
 * Date:     2020/8/11 14:17
 * Description: 申请令牌测试
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.auth;

import com.sun.jersey.core.util.Base64;
import com.xuecheng.framework.client.XcServiceList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br> 
 * 〈申请令牌测试〉
 * @author admin
 * @create 2020/8/11
 * @since 1.0.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestClient {

    @Autowired
    LoadBalancerClient loadBalancerClient;

    @Autowired
    RestTemplate restTemplate;

    @Test
    public void testClient(){
        ServiceInstance choose = loadBalancerClient.choose(XcServiceList.XC_SERVICE_UCENTER_AUTH);
        URI uri = choose.getUri();
        String authUrl=uri+"/auth/oauth/token";
        //请求内容分两部分，一部分是header 一部分是内容
        //header信息，包含了http basic信息
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        String httpbasic=httpbasic("XcWebApp","XcWebApp");
        headers.add("Authorization",httpbasic);
        // 包括grant_type username password
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type","password");
        body.add("username","itcast");
        body.add("password","123");
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
        Map body1 = exchange.getBody();
        System.out.println(body1);

    }

    private String httpbasic(String clientId, String secret) {
        //将客户端id和客户端密码进行拼接,按"客户端id:客户端密码"
        String string=clientId+":"+secret;
        //进行base64编码
        byte[] encode = Base64.encode(string.getBytes());
        return "Basic "+new String(encode);
    }

}
