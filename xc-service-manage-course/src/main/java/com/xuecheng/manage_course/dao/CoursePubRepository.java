/**
 * FileName: CoursePubRepository
 * Author:   admin
 * Date:     2020/8/4 15:13
 * Description: dao
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CoursePub;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 〈一句话功能简述〉<br> 
 * 〈dao〉
 * @author admin
 * @create 2020/8/4
 * @since 1.0.0
 */
public interface CoursePubRepository extends JpaRepository<CoursePub,String> {
}
