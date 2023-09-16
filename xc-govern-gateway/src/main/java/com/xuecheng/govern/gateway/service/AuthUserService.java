/**
 * FileName: AuthUserService
 * Author:   admin
 * Date:     2020/8/13 17:50
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.govern.gateway.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xuecheng.framework.domain.ucenter.ext.AuthToken;
import com.xuecheng.framework.utils.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 * @author admin
 * @create 2020/8/13
 * @since 1.0.0
 */
@Service
public class AuthUserService {

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 从header中取出jwt令牌
     * @param request
     * @return
     */
    public String getJwtFromHeader(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (StringUtils.isEmpty(authorization)){
            return null;
        }
        //如果不是以这个字符开头的 返回null
        if (!authorization.startsWith("Bearer ")){
            return null;
        }
        //取到jwt令牌
        String substring = authorization.substring(7);
        return substring;
    }

    /**
     * 从cookies中取出token令牌
     * @param request
     * @return
     */
    public String getTokenFromCookies(HttpServletRequest request) {
        Map<String, String> uid = CookieUtil.readCookie(request, "uid");
        String authToken = uid.get("uid");
        return authToken;
    }

    /**
     * 获取令牌过期时间
     * @param token
     * @return
     */
    public Long getJwtTimeFromRedis(String token) {
        String key="user_token:"+token;
        Long expire = redisTemplate.getExpire(key);
        return expire;
    }
}
