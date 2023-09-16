/**
 * FileName: CmsResponsity
 * Author:   admin
 * Date:     2020/6/8 10:20
 * Description: dao层
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈dao层〉
 * @author admin
 * @create 2020/6/8
 * @since 1.0.0
 */
public interface CmsResponsity extends MongoRepository<CmsPage,Object> {
    List<CmsPage> findByPageNameAndSiteIdAndPageId(String pageName, String siteId, String pageId);
}
