/**
 * FileName: TeachplanBaseRepository
 * Author:   admin
 * Date:     2020/7/16 14:47
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.Teachplan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 * @author admin
 * @create 2020/7/16
 * @since 1.0.0
 */
public interface TeachplanBaseRepository extends JpaRepository<Teachplan,String> {
     List<Teachplan> findByCourseidAndParentid(String courseid, String parentId);
}
