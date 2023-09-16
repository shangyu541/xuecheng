/**
 * FileName: CmsConfigController
 * Author:   admin
 * Date:     2020/6/21 11:28
 * Description: cms静态页面展示查询接口
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsConfigControllerApi;
import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.manage_cms.service.CmsConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 〈一句话功能简述〉<br> 
 * 〈cms静态页面展示查询接口〉
 * @author admin
 * @create 2020/6/21
 * @since 1.0.0
 */
@Api(description = "cms静态页面查询展示接口")
@RestController
public class CmsConfigController implements CmsConfigControllerApi {

    @Autowired
    private CmsConfigService cmsConfigService;

    @ApiOperation("根据id查询cms静态页面数据")
    @GetMapping("/getModel/{id}")
    @Override
    public CmsConfig getModel(@PathVariable("id") String id) {
        return cmsConfigService.getModel(id);
    }
}
