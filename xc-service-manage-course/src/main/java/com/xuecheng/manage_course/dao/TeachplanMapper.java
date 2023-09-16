/**
 * FileName: TeachplanMapper
 * Author:   admin
 * Date:     2020/7/16 13:48
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 * @author admin
 * @create 2020/7/16
 * @since 1.0.0
 */
@Mapper
public interface TeachplanMapper {

    TeachplanNode selectList(@Param("courseId") String courseId);
}
