/**
 * FileName: CmsConfigControllerApi
 * Author:   admin
 * Date:     2020/6/21 11:24
 * Description: cms 静态页面配置管理接口
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 〈一句话功能简述〉<br> 
 * 〈cms 静态页面配置管理接口〉
 * @author admin
 * @create 2020/6/21
 * @since 1.0.0
 */
@Api(value = "cms配置管理接口",description = "cms配置管理接口，根据数据模型得管理，查询接口")
public interface CmsConfigControllerApi {

    @ApiOperation(value = "根据id查询cms配置信息")
    public CmsConfig getModel(String id);
}
