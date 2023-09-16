/**
 * FileName: CourseService
 * Author:   admin
 * Date:     2020/7/16 13:45
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.CourseView;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 * @author admin
 * @create 2020/7/16
 * @since 1.0.0
 */
public interface CourseService {
    TeachplanNode findTeachplanList(String courseId);

    ResponseResult saveTeachplan(Teachplan teachplan);

    CourseView getCourseView(String id);

    QueryResponseResult findCourseList(String companyId, int page, int size, CourseListRequest courseListRequest);
}
