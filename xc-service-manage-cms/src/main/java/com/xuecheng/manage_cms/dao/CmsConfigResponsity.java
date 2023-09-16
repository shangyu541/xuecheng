/**
 * FileName: CmsConfigResponsity
 * Author:   admin
 * Date:     2020/6/21 11:31
 * Description: Cms静态页面查询展示接口
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 〈一句话功能简述〉<br> 
 * 〈Cms静态页面查询展示接口〉
 * @author admin
 * @create 2020/6/21
 * @since 1.0.0
 */
public interface CmsConfigResponsity extends MongoRepository<CmsConfig,Object> {
}
