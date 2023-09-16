/**
 * FileName: CourseMarketRepository
 * Author:   admin
 * Date:     2020/7/30 17:18
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CourseMarket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 * @author admin
 * @create 2020/7/30
 * @since 1.0.0
 */
public interface CourseMarketRepository extends JpaRepository<CourseMarket,String> {
}
