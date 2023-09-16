package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.ApiOperation;

import java.io.IOException;

/**
 * 微服务远程调用使用
 */
public interface CmsPageControllerApi {

    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);

    public CmsPage findById(String id);

    public CmsPageResult updateCmsPage(String id,CmsPage cmsPage);

    public ResponseResult deleteById(String id);

    @ApiOperation("页面发布")
    public ResponseResult post(String pageId) throws IOException;
}
