/**
 * FileName: AuthControllerApi
 * Author:   admin
 * Date:     2020/8/11 14:12
 * Description: 用户认证api
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.api.oauth;

import com.xuecheng.framework.domain.ucenter.request.LoginRequest;
import com.xuecheng.framework.domain.ucenter.response.JwtResult;
import com.xuecheng.framework.domain.ucenter.response.LoginResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 〈一句话功能简述〉<br> 
 * 〈用户认证api〉
 * @author admin
 * @create 2020/8/11
 * @since 1.0.0
 */
@Api(tags = "用户认证接口api")
public interface AuthControllerApi {

    @ApiOperation("登录接口")
    public LoginResult login(LoginRequest loginRequest);

    @ApiOperation("退出接口")
    public ResponseResult logout();


    @ApiOperation("查询用户jwt令牌")
    public JwtResult getUserJwt();

}
