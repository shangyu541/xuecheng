/**
 * FileName: CmsTemplateResponsity
 * Author:   admin
 * Date:     2020/6/23 10:57
 * Description: 模板查询
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 〈一句话功能简述〉<br> 
 * 〈模板查询〉
 * @author admin
 * @create 2020/6/23
 * @since 1.0.0
 */
public interface CmsTemplateResponsity extends MongoRepository<CmsTemplate,Object> {
}
