/**
 * FileName: AuthController
 * Author:   admin
 * Date:     2020/8/12 10:38
 * Description: 权限认证controller
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.auth.controller;

import com.alibaba.fastjson.JSON;
import com.xuecheng.api.oauth.AuthControllerApi;
import com.xuecheng.auth.client.UserClient;
import com.xuecheng.auth.service.AuthService;
import com.xuecheng.framework.domain.ucenter.ext.AuthToken;
import com.xuecheng.framework.domain.ucenter.request.LoginRequest;
import com.xuecheng.framework.domain.ucenter.response.AuthCode;
import com.xuecheng.framework.domain.ucenter.response.JwtResult;
import com.xuecheng.framework.domain.ucenter.response.LoginResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.utils.CookieUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br> 
 * 〈权限认证controller〉
 * @author admin
 * @create 2020/8/12
 * @since 1.0.0
 */
@Api(tags = "权限认证")
@RestController
@RequestMapping("/")
public class AuthController implements AuthControllerApi {

    @Autowired
    AuthService authService;

    @Autowired
    RedisTemplate redisTemplate;

    @Value("${auth.clientId}")
    String clientId;

    @Value("${auth.cookieDomain}")
    String cookieDomain;

    @Value("${auth.cookieMaxAge}")
    int cookieMaxAge;

    @Value("${auth.clientSecret}")
    String clientSecret;

    @ApiOperation("登录接口")
    @PostMapping("/userLogin")
    @Override
    public LoginResult login(LoginRequest loginRequest) {
        if (loginRequest==null|| StringUtils.isEmpty(loginRequest.getUsername())){
            ExceptionCast.cast(AuthCode.AUTH_USERNAME_NONE);
        }
        if (loginRequest==null|| StringUtils.isEmpty(loginRequest.getPassword())){
            ExceptionCast.cast(AuthCode.AUTH_PASSWORD_NONE);
        }
        String password = loginRequest.getPassword();
        String username = loginRequest.getUsername();
        AuthToken authToken=authService.login(password,username,clientId,clientSecret);
        String access_token = authToken.getAccess_token();
        //将令牌写入cookie
        saveCookie(access_token);
        return new LoginResult(CommonCode.SUCCESS,access_token);
    }


    private void saveCookie(String authToken) {
        HttpServletResponse response=((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
        CookieUtil.addCookie(response,cookieDomain,"/","uid",authToken,cookieMaxAge,false);
    }

    /**
     * 流程步骤:删除redis中的token
     * 清楚cookie
     * @param
     * @return
     */
    @ApiOperation("退出接口")
    @PostMapping("/userLogout")
    @Override
    public ResponseResult logout() {
        //取出令牌
        String cookieByJwt = getCookieByJwt();
        //删除redis中的token
        boolean result=authService.deleteToken(cookieByJwt);
        if (!result){
            ExceptionCast.cast(AuthCode.AUTH_LOGOUT_FAIL);
        }
        //清除cookies
        this.clearCookies(cookieByJwt);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    private void clearCookies(String cookieByJwt) {
        HttpServletResponse response=((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
        CookieUtil.addCookie(response,cookieDomain,"/","uid",cookieByJwt,cookieMaxAge,false);
    }


    /**
     * 流程步骤：1.取出cookie中的用户身份令牌
     *  拿身份令牌从redis中查询jwt令牌
     *  将jwt令牌返回给用户
     * @return
     */
    @ApiOperation("jwt查询接口")
    @GetMapping("/getUserJwt")
    @Override
    public JwtResult getUserJwt() {
        //1
        String cookies=this.getCookieByJwt();
        //2
        if (cookies==null){
            return new JwtResult(CommonCode.FAIL,null);
        }
        AuthToken authToken=getUserToken(cookies);
        if (authToken!=null){
            String jwt_token = authToken.getJwt_token();
            return new JwtResult(CommonCode.SUCCESS,jwt_token);
        }
        return new JwtResult(CommonCode.UNAUTHORISE,null);
    }

    /**
     * 通过jwt令牌获取redis中的authToken信息
     * @param cookies
     * @return
     */
    private AuthToken getUserToken(String cookies) {
        String key ="user_token:"+cookies;
        String o = (String) redisTemplate.opsForValue().get(key);
//        AuthToken authToken = JSON.parseObject(o,AuthToken.class);
        AuthToken authToken=null;
        try {
             authToken = JSON.parseObject(o,AuthToken.class);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return authToken;
    }

    private String getCookieByJwt() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Map<String, String> map = CookieUtil.readCookie(request, "uid");
        if (map!=null && map.get("uid")!=null){
            String uid=map.get("uid");
            return uid;
        }
        return null;
    }
}
