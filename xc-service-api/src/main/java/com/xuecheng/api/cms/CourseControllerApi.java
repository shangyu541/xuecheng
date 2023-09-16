/**
 * FileName: CourseControllerApi
 * Author:   admin
 * Date:     2020/7/16 13:12
 * Description: 课程计划接口api
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.course.CourseView;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * 〈一句话功能简述〉<br> 
 * 〈课程计划接口api〉
 * @author admin
 * @create 2020/7/16
 * @since 1.0.0
 */
@Api(tags = "课程功能接口api，提供课程管理的增删改查")
public interface CourseControllerApi {
    @ApiOperation("课程计划查询")
    public TeachplanNode findTeachplanList(String courseId);

    @ApiOperation("课程计划新增")
    public ResponseResult saveTeachplan(Teachplan teachplan);

    @ApiOperation("课程视图查询")
    public CourseView courseView(String id);

    @ApiOperation("课程视图查询")
    public QueryResponseResult findCourseList(int page, int size, CourseListRequest courseListRequest);
}
