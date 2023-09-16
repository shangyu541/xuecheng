/**
 * FileName: CmsPageClient
 * Author:   admin
 * Date:     2020/7/30 14:56
 * Description: cmspage调用接口
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.manage_course.client;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.interceptor.FeignClientInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 〈一句话功能简述〉<br> 
 * 〈cmspage调用接口〉
 * @author admin
 * @create 2020/7/30
 * @since 1.0.0
 */
@FeignClient(value = "XC-SERVICE-MANAGE-CMS",configuration = FeignClientInterceptor.class)//指定远程调用的服务名
public interface CmsPageClient {
/*
    SpringCloud对Feign进行了增强兼容了SpringMVC的注解 ，我们在使用SpringMVC的注解时需要注意：
        1、feignClient接口 有参数在参数必须加@PathVariable("XXX")和@RequestParam("XXX")
        2、feignClient返回值为复杂对象时其类型必须有无参构造函数。*/
    @GetMapping("/cms/page/findById/{id}")
    public CmsPage findCmsPageById(@PathVariable("id")String id);
}
