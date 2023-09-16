/**
 * FileName: CmsPageController
 * Author:   admin
 * Date:     2020/6/5 16:32
 * Description: 测试工程
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.service.CmsPageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * 〈一句话功能简述〉<br>
 * 〈测试工程〉
 *
 * @author admin
 * @create 2020/6/5
 * @since 1.0.0
 */
@Api(description = "cms测试工程页面")
@RestController
@RequestMapping("/cms/page")
public class CmsPageController implements CmsPageControllerApi {
    @Autowired
    CmsPageService cmsPageService;

    @Value("${server.port}")
    String port;

    @ApiOperation("分页查询接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "页码"),
            @ApiImplicitParam(name = "size",value = "条数"),
    })
    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult findList(@PathVariable("page") int page, @PathVariable("size") int size, QueryPageRequest queryPageRequest) {
        /*QueryResult<CmsPage> result = new QueryResult<>();
        List<CmsPage> list = new ArrayList<>();
        CmsPage cmsPage=new CmsPage();
        cmsPage.setPageId("1");
        cmsPage.setPageName("测试页面");
        list.add(cmsPage);
        result.setList(list);
        result.setTotal(1);
        QueryResponseResult queryResponseResult=new QueryResponseResult(CommonCode.SUCCESS,result);
        */
        QueryResponseResult list = cmsPageService.findList(page, size, queryPageRequest);
        return list;
    }

    @ApiOperation("根据id进行查询")
    @Override
    @GetMapping("/findById/{id}")
    public CmsPage findById(@PathVariable("id") String id) {
        CmsPage list = cmsPageService.findById(id);
        System.out.println("测试负载:"+port);
        return list;
    }

    @ApiOperation("修改程序（简写了，好多没加）")
    @Override
    @GetMapping("/updateCmsPage/{id}")
    public CmsPageResult updateCmsPage(@PathVariable("id") String id,@RequestBody CmsPage cmsPage) {
        CmsPageResult cmsPageResult=cmsPageService.updateCmsPage(id,cmsPage);
        return cmsPageResult;
    }

    @ApiOperation("根据id进行删除")
    @DeleteMapping("/deleteById/{id}")
    @Override
    public ResponseResult deleteById(@PathVariable("id") String id) {
        return cmsPageService.deleteById(id);

    }

    @ApiOperation("页面发布接口")
    @Override
    @GetMapping("/postPage/{pageId}")
    public ResponseResult post(@PathVariable("pageId") String pageId) throws IOException {
        return cmsPageService.post(pageId);
    }

    @ApiOperation("新增")
    @PostMapping("/saveCmsPge")
    public CmsPageResult saveCmsPge(@RequestBody CmsPage cmsPage) {
        return cmsPageService.saveCmsPage(cmsPage);
    }

}
