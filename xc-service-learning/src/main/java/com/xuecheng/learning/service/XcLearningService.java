/**
 * FileName: XcLearningService
 * Author:   admin
 * Date:     2020/8/20 13:09
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.learning.service;

import com.xuecheng.framework.domain.learning.LearningCode;
import com.xuecheng.framework.domain.learning.XcLearningCourse;
import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.framework.domain.task.XcTaskHis;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.learning.dao.XcLearningCourseRepository;
import com.xuecheng.learning.dao.XcTaskHisRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 * @author admin
 * @create 2020/8/20
 * @since 1.0.0
 */
@Service
public class XcLearningService {

    @Autowired
    XcLearningCourseRepository xcLearningCourseRepository;

    @Autowired
    XcTaskHisRepository xcTaskHisRepository;

    /**
     * 完成选课
     * @param userId
     * @param courseId
     * @param valid
     * @param startTime
     * @param endTime
     * @param xcTask
     * @return
     */
    @Transactional
    public ResponseResult addCourse(String userId, String courseId, String valid, Date startTime, Date endTime, XcTask xcTask) {
        if (StringUtils.isEmpty(courseId)){
            ExceptionCast.cast(LearningCode.LEARNING_GETMEDIA_ERROR);
        }
        if (StringUtils.isEmpty(userId)){
            ExceptionCast.cast(LearningCode.LEARNING_USERID_ERROR);
        }
        if (xcTask==null || StringUtils.isEmpty(xcTask.getId())){
            ExceptionCast.cast(LearningCode.LEARNING_XCTASK_ERROR);
        }
        //查询历史任务，如果历史任务中有 就直接返回true 这个订单已在资源中
        Optional<XcTaskHis> byId = xcTaskHisRepository.findById(xcTask.getId());
        if (byId.isPresent()){
            return new ResponseResult(CommonCode.SUCCESS);
        }
        XcLearningCourse xcLearningCourse=xcLearningCourseRepository.findXcLearningCourseByUserIdAndCourseId(userId,courseId);
        //如果没有选课记录，则添加
        if (xcLearningCourse==null){
            xcLearningCourse=new XcLearningCourse();
            xcLearningCourse.setCourseId(courseId);
            xcLearningCourse.setStartTime(startTime);
            xcLearningCourse.setEndTime(endTime);
            xcLearningCourse.setUserId(userId);
            xcLearningCourse.setValid(valid);
            xcLearningCourse.setStatus("501001");
            xcLearningCourseRepository.save(xcLearningCourse);
        }else {
            xcLearningCourse.setValid(valid);
            xcLearningCourse.setEndTime(endTime);
            xcLearningCourse.setStartTime(startTime);
            xcLearningCourse.setStatus("501001");
            xcLearningCourseRepository.save(xcLearningCourse);
        }
        //向历史任务表中插入记录
        Optional<XcTaskHis> byId1 = xcTaskHisRepository.findById(xcTask.getId());
        if (!byId1.isPresent()){
            XcTaskHis xcTaskHis=new XcTaskHis();
            BeanUtils.copyProperties(xcTask,xcTaskHis);
            xcTaskHisRepository.save(xcTaskHis);
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }
}
