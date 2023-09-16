/**
 * FileName: xcTaskHisRepository
 * Author:   admin
 * Date:     2020/8/20 13:54
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.learning.dao;

import com.xuecheng.framework.domain.task.XcTaskHis;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 〈一句话功能简述〉<br> 
 * 〈查询历史任务〉
 * @author admin
 * @create 2020/8/20
 * @since 1.0.0
 */
public interface XcTaskHisRepository extends JpaRepository<XcTaskHis,String> {
}
