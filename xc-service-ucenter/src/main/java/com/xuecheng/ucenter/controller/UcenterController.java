/**
 * FileName: UcenterController
 * Author:   admin
 * Date:     2020/8/12 15:17
 * Description: 用户
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.ucenter.controller;

import com.xuecheng.api.user.XcUserControllerApi;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import com.xuecheng.ucenter.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 〈一句话功能简述〉<br> 
 * 〈用户〉
 * @author admin
 * @create 2020/8/12
 * @since 1.0.0
 */
@Api(tags = "用户认证")
@RestController
@RequestMapping("/ucenter")
public class UcenterController implements XcUserControllerApi {

    @Autowired
    UserService userService;


    @GetMapping("/getUserExt")
    @Override
    public XcUserExt getUserExt(@RequestParam("username") String username) {
        return userService.getUserExt(username);
    }
}
