/**
 * FileName: EsCourseController
 * Author:   admin
 * Date:     2020/8/5 10:07
 * Description: es搜索api
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Tag;

/**
 * 〈一句话功能简述〉<br> 
 * 〈es搜索api〉
 * @author admin
 * @create 2020/8/5
 * @since 1.0.0
 */
@Api(tags = "es搜索api")
public interface EsCourseControllerApi {

    @ApiOperation("s")
    public QueryResponseResult list(int page, int size,
                                    CourseSearchParam courseSearchParam);
}
