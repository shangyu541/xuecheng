/**
 * FileName: CourseServiceImpl
 * Author:   admin
 * Date:     2020/7/16 13:46
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.manage_course.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.*;
import com.xuecheng.manage_course.service.CourseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 * @author admin
 * @create 2020/7/16
 * @since 1.0.0
 */
@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    TeachplanMapper teachplanMapper;

    @Autowired
    TeachplanBaseRepository teachplanBaseRepository;

    @Autowired
    CourseBaseRepository courseBaseRepository;

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    CourseMarketRepository courseMarketRepository;

    @Autowired
    CoursePicRepository coursePicRepository;


    @Override
    public TeachplanNode findTeachplanList(String courseId) {
        return teachplanMapper.selectList(courseId);
    }

    @Override
    public ResponseResult saveTeachplan(Teachplan teachplan) {
        if (teachplan==null ||
                StringUtils.isEmpty(teachplan.getCourseid()) ||
                StringUtils.isEmpty(teachplan.getPname())){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        String courseid = teachplan.getCourseid();
        String parentid = teachplan.getParentid();
        if (StringUtils.isEmpty(parentid)){
            //取出该课程的根节点
            parentid=this.getTeachplanRoot(courseid);
        }
        Optional<Teachplan> byId = teachplanBaseRepository.findById(parentid);
        Teachplan parentNode = byId.get();
        //获取父节点的级别
        String grade = parentNode.getGrade();
        Teachplan teachplanNew = new Teachplan();
        //讲页面提交的数据拷贝到new中
        BeanUtils.copyProperties(teachplan,teachplanNew);
        teachplanNew.setParentid(parentid);
        teachplanNew.setCourseid(courseid);
        if ("1".equals(grade)) {
            teachplanNew.setGrade("2");
        }else {
            teachplanNew.setGrade("3");
        }
        teachplanBaseRepository.save(teachplanNew);

        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Override
    public CourseView getCourseView(String id) {
        CourseView courseView=new CourseView();
        Optional<CourseBase> byId = courseBaseRepository.findById(id);
        if (byId.isPresent()) {
            courseView.setCourseBase(byId.get());
        }
        Optional<CourseMarket> courseMarket = courseMarketRepository.findById(id);
        if (courseMarket.isPresent()) {
            courseView.setCourseMarket(courseMarket.get());
        }

        Optional<CoursePic> coursePic = coursePicRepository.findById(id);
        if (coursePic.isPresent()) {
            courseView.setCoursePic(coursePic.get());
        }
        TeachplanNode teachplanNode = teachplanMapper.selectList(id);
        if (teachplanNode!=null) {
            courseView.setTeachplanNode(teachplanNode);
        }
        return courseView;
    }

    @Override
    public QueryResponseResult findCourseList(String companyId, int page, int size, CourseListRequest courseListRequest) {
        if (courseListRequest==null){
            courseListRequest=new CourseListRequest();
        }
        courseListRequest.setCompanyId(companyId);
        PageHelper.startPage(page, size);
        Page<CourseInfo> courseList=courseMapper.findCourseListPage(courseListRequest);
        long totalPages = courseList.getTotal();
        List<CourseInfo> content = courseList.getResult();
        QueryResult objectQueryResult = new QueryResult<>();
        objectQueryResult.setList(content);
        objectQueryResult.setTotal(totalPages);
        return new QueryResponseResult(CommonCode.SUCCESS,objectQueryResult);
    }

    /**
     * 查询课程的根节点，如果查询不到要自动添加一个
     * @param courseid
     * @return
     */
    private String getTeachplanRoot(String courseid) {
        Optional<CourseBase> byId = courseBaseRepository.findById(courseid);
        if (!byId.isPresent()){
            return null;
        }
        CourseBase courseBase = byId.get();
        List<Teachplan> teachplanList=teachplanBaseRepository.findByCourseidAndParentid(courseid,"0");
        if (CollectionUtils.isEmpty(teachplanList)){
        //查询不到，自动添加根节点
            Teachplan teachplan = new Teachplan();
            teachplan.setParentid("0");
            teachplan.setCourseid(courseid);
            teachplan.setGrade("1");
            teachplan.setStatus("0");
            teachplan.setPname(courseBase.getName());
            Teachplan save = teachplanBaseRepository.save(teachplan);
            return save.getId();
        }
        //返回根节点
        return teachplanList.get(0).getId();
    }
}
