/**
 * FileName: FeignClientInterceptor
 * Author:   admin
 * Date:     2020/8/17 15:46
 * Description: Feign拦截器
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.framework.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * 〈一句话功能简述〉<br> 
 * 〈Feign拦截器〉
 * @author admin
 * @create 2020/8/17
 * @since 1.0.0
 */
public class FeignClientInterceptor implements RequestInterceptor {

    //每次请求feign都回调用这个apply方法
    @Override
    public void apply(RequestTemplate requestTemplate) {
        //1.要找出当前请求的header，找到jwt令牌
        //2.将jwt令牌向下传递
        ServletRequestAttributes attributes= ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        if (attributes!=null){
            HttpServletRequest request = attributes.getRequest();
            //取出当前的请求，找到jwt令牌
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames!=null){
                while (headerNames.hasMoreElements()){
                    String headerName = headerNames.nextElement();
                    String headerValue = request.getHeader(headerName);
                    if (headerName.equals("Authorization")){
                        //向下传递
                        requestTemplate.header(headerName,headerValue);
                    }
                }
            }
        }

    }
}
