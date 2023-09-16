/**
 * FileName: CourseController
 * Author:   admin
 * Date:     2020/7/16 13:44
 * Description: 课程计划接口
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.manage_course.controller;

import com.xuecheng.api.cms.CourseControllerApi;
import com.xuecheng.framework.domain.course.CourseView;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.utils.XcOauth2Util;
import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage_course.service.CourseService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 〈一句话功能简述〉<br> 
 * 〈课程计划接口〉
 * @author admin
 * @create 2020/7/16
 * @since 1.0.0
 */
@Api(tags = "课程计划接口")
@RestController
public class CourseController extends BaseController implements CourseControllerApi {

    @Autowired
    CourseService courseService;

    @PreAuthorize("hasAuthority('course_find_list')")
    @GetMapping("/course/find/{courseId}")
    @Override
    public TeachplanNode findTeachplanList(@PathVariable("courseId") String courseId) {
        return courseService.findTeachplanList(courseId);
    }

    @Override
    @PostMapping("/course/save")
    public ResponseResult saveTeachplan(@RequestBody Teachplan teachplan) {
        return courseService.saveTeachplan(teachplan);
    }

    @GetMapping("/course/courseView/{id}")
    @Override
    public CourseView courseView(@PathVariable("id") String id) {

        return courseService.getCourseView(id);
    }

    /**
     * 每个用户只能查询自己的课程
     * @param page
     * @param size
     * @param courseListRequest
     * @return
     */
    @GetMapping("/course/findCourseList/{page}/{size}")
    @Override
    public QueryResponseResult findCourseList(@PathVariable("page") int page, @PathVariable("size")int size, CourseListRequest courseListRequest) {
        //获取用户信息
        XcOauth2Util xcOauth2Util = new XcOauth2Util();
        XcOauth2Util.UserJwt userJwtFromHeader = xcOauth2Util.getUserJwtFromHeader(request);
        //获取企业id
        String companyId = userJwtFromHeader.getCompanyId();
        return courseService.findCourseList(companyId,page,size,courseListRequest);
    }

}
