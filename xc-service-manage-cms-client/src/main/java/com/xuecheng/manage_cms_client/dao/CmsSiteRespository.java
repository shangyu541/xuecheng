/**
 * FileName: CmsSiteRespository
 * Author:   admin
 * Date:     2020/7/14 15:41
 * Description: 查询站点信息
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.manage_cms_client.dao;

import com.xuecheng.framework.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 〈一句话功能简述〉<br> 
 * 〈查询站点信息〉
 * @author admin
 * @create 2020/7/14
 * @since 1.0.0
 */
public interface CmsSiteRespository extends MongoRepository<CmsSite,String> {
}
