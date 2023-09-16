/**
 * FileName: LoginFilterTest
 * Author:   admin
 * Date:     2020/8/13 17:11
 * Description: 过滤器
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.govern.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 〈一句话功能简述〉<br> 
 * 〈过滤器〉
 * @author admin
 * @create 2020/8/13
 * @since 1.0.0
 */
//@Component
public class LoginFilterTest extends ZuulFilter {

    /**
     * 过滤器的类型
     * @return
     */
    @Override
    public String filterType() {
        /**
         * pre 请求在被路由之前执行
         *
         * routing 在路由请求时调用
         *
         * post 在routing和error过滤器之后调用
         *
         * error 在处理错误请求时调用
         */
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        //返回true表示要执行该过滤器
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        HttpServletResponse response = currentContext.getResponse();
        //得到Authorization头
        String authorization = request.getHeader("Authorization");
        if (StringUtils.isEmpty(authorization)){
            //拒绝访问
            currentContext.setSendZuulResponse(false);
            //设置响应代码
            currentContext.setResponseStatusCode(200);
            ResponseResult responseResult = new ResponseResult(CommonCode.UNAUTHENTICATED);
            String s = JSON.toJSONString(responseResult);
            currentContext.setResponseBody(s);
            //设置contentType
            response.setContentType("application/json;charset=utf-8");
            return null;
        }
        return null;
    }
}
