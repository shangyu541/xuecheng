/**
 * FileName: XcUserControllerApi
 * Author:   admin
 * Date:     2020/8/12 14:57
 * Description: userAPI接口
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.api.user;

import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 〈一句话功能简述〉<br> 
 * 〈userAPI接口〉
 * @author admin
 * @create 2020/8/12
 * @since 1.0.0
 */
@Api(tags = "用户中心管理")
public interface XcUserControllerApi {

    @ApiOperation("用户中心")
    public XcUserExt getUserExt(String username);
}
