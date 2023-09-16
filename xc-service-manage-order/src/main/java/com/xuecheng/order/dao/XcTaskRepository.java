/**
 * FileName: XcTaskRepository
 * Author:   admin
 * Date:     2020/8/18 16:29
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.order.dao;

import com.xuecheng.framework.domain.task.XcTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 * @author admin
 * @create 2020/8/18
 * @since 1.0.0
 */
public interface XcTaskRepository extends JpaRepository<XcTask,String> {
    Page<XcTask> findByUpdateTimeBefore(Pageable pageRequest, Date updateTime);

    @Modifying
    @Query("update XcTask SET version= :version+1 where id=:id and version=:version")
    int updateVersion(@Param("id") String id, @Param("version") Integer version);
}
