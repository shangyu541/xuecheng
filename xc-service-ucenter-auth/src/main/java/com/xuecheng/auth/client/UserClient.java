/**
 * FileName: UserClient
 * Author:   admin
 * Date:     2020/8/12 15:41
 * Description: 远程调用
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.auth.client;

import com.xuecheng.framework.client.XcServiceList;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 〈一句话功能简述〉<br> 
 * 〈远程调用〉
 * @author admin
 * @create 2020/8/12
 * @since 1.0.0
 */
@FeignClient(value = XcServiceList.XC_SERVICE_UCENTER)
public interface UserClient {
    @GetMapping("/ucenter/getUserExt")
    public XcUserExt getUserExt(@RequestParam("username") String username);
}
