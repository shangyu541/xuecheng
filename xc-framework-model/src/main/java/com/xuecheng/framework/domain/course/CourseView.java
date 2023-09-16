/**
 * FileName: CourseView
 * Author:   admin
 * Date:     2020/7/30 17:06
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.framework.domain.course;

import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 〈课程数据模型〉<br>
 * 〈〉
 * @author admin
 * @create 2020/7/30
 * @since 1.0.0
 */
@Data
@ToString
@NoArgsConstructor
public class CourseView implements Serializable {
    //基础信息
    CourseBase courseBase;
    //课程营销
    CourseMarket courseMarket;
    //课程图片
    CoursePic coursePic;
    //教学计划
    TeachplanNode teachplanNode;
}
