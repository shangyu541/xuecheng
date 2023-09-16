/**
 * FileName: TestJwt
 * Author:   admin
 * Date:     2020/8/11 10:54
 * Description: jwt生成测试
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.auth;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.util.HashMap;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br> 
 * 〈jwt生成测试〉
 * @author admin
 * @create 2020/8/11
 * @since 1.0.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestJwt {

    //生成jwt令牌
    @Test
    public void testCreatJwt(){
        //密钥文件
        String keystore="xc.keystore";
        //密钥库的密码
        String keystore_password="xuechengkeystore";

        //访问证书文件路径
        ClassPathResource classPathResource = new ClassPathResource(keystore);
        //密钥别名
        String alias="xckey";
        //密钥的访问密码
        String key_password="xuecheng";
        //密钥工厂
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(classPathResource,keystore_password.toCharArray());
        //密钥对（公司钥）
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias, key_password.toCharArray());
        //获取私钥
        RSAPrivateCrtKey aPrivate = (RSAPrivateCrtKey) keyPair.getPrivate();
        //生成jwt令牌
        Map map=new HashMap<>();
        map.put("name","itcast");
        String s = JSON.toJSONString(map);
        Jwt encode = JwtHelper.encode(s, new RsaSigner(aPrivate));
        //生成jwt令牌编码
        String encoded = encode.getEncoded();
        System.out.println(encoded);
    }

    //校验jwt令牌
    @Test
    public void testJwt(){
        //公钥
        String publicKey="-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnASXh9oSvLRLxk901HANYM6KcYMzX8vFPnH/To2R+SrUVw1O9rEX6m1+rIaMzrEKPm12qPjVq3HMXDbRdUaJEXsB7NgGrAhepYAdJnYMizdltLdGsbfyjITUCOvzZ/QgM1M4INPMD+Ce859xse06jnOkCUzinZmasxrmgNV3Db1GtpyHIiGVUY0lSO1Frr9m5dpemylaT0BV3UwTQWVW9ljm6yR3dBncOdDENumT5tGbaDVyClV0FEB1XdSKd7VjiDCDbUAUbDTG1fm3K9sx7kO1uMGElbXLgMfboJ963HEJcU01km7BmFntqI5liyKheX+HBUCD4zbYNPw236U+7QIDAQAB-----END PUBLIC KEY-----";
        //jwt令牌
//        String token="eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1lIjoiaXRjYXN0In0.lQOqL1s4DpDHROUAibkz6EMf6hcM7HmTPgmg-SlkacVoQAV7y3XQ7LXxiua6SJlN_uNX_EFjzIshEg_kyy972DtymtRMc2NIO5HzIF5I4oQCxNPsJdhu6qQni6sTas3q0JbAarMZSajDX7HhzVSYWPQJCussA4e1r9oFxDcoAo6TEAXOW8gRHzNIygQz1yCj6mdf4UOHI070kRy7f3BdhmrUJdOuDIMoRBYS4WsEOibAU1UCNPaJAXpZC0ihrtdY7SCg1N43fimeFOHrfpLb6OmRF7v7uvGMgrhg9JIYDbJ6nbode5OJkNceRx8QUICre2yKAe0ctlvXO0REf6OpRA";
        String token="eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOiIxIiwidXNlcnBpYyI6bnVsbCwidXNlcl9uYW1lIjoiaXRjYXN0Iiwic2NvcGUiOlsiYXBwIl0sIm5hbWUiOiJ0ZXN0MDIiLCJ1dHlwZSI6IjEwMTAwMiIsImlkIjoiNDkiLCJleHAiOjE1OTc2ODQ4NzcsImF1dGhvcml0aWVzIjpbInhjX3RlYWNobWFuYWdlcl9jb3Vyc2VfYmFzZSIsInhjX3RlYWNobWFuYWdlcl9jb3Vyc2VfZGVsIiwieGNfdGVhY2htYW5hZ2VyX2NvdXJzZV9saXN0IiwieGNfdGVhY2htYW5hZ2VyX2NvdXJzZV9wbGFuIiwieGNfdGVhY2htYW5hZ2VyX2NvdXJzZSIsImNvdXJzZV9maW5kX2xpc3QiLCJ4Y190ZWFjaG1hbmFnZXIiLCJ4Y190ZWFjaG1hbmFnZXJfY291cnNlX21hcmtldCIsInhjX3RlYWNobWFuYWdlcl9jb3Vyc2VfcHVibGlzaCIsInhjX3RlYWNobWFuYWdlcl9jb3Vyc2VfYWRkIl0sImp0aSI6ImViZmI3Y2E0LTBiMmMtNGI1OC05ZDczLWYyMWMwMjlmMTJiOCIsImNsaWVudF9pZCI6IlhjV2ViQXBwIn0.C7EFxPGEsyqgeiVsCHiQDgyFdRV2ctMix_dvGHZm0i3BTIX8fy6dkujRHRe5FHPq--3isXTOoMM3mJTsrcA0ph7ogByTYIJszo3HrjhFjwgqCWgEE9uEA1sfX1Bhierl5OHbkacQmbUeHmb4RINiVeTnqQjFdLE7EVHp0B1lLJpy1bWeumcx3ZWntjVTMbckNV69LBFWGggJ874ad0VPdsnsegLN364AGlLYpWy200lqTpf_mSmT_LCTdpu3HTRyaL4Gxd548D9UFc4tT0-Gr0XPueJ4IY_IqFoDB2Pn4qSYaAjryTGUCvV72K2yHcNWbTpLGlZRcupBQnnGPW7WMA";
        //校验jwt
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publicKey));
        //获取jwt原始内容
        String claims = jwt.getClaims();
        String encoded = jwt.getEncoded();
        System.out.println(claims);

    }
}
