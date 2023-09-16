/**
 * FileName: CmsConfigService
 * Author:   admin
 * Date:     2020/6/21 11:35
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.manage_cms.dao.CmsConfigResponsity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 * @author admin
 * @create 2020/6/21
 * @since 1.0.0
 */
@Service
public class CmsConfigService {

    @Autowired
    CmsConfigResponsity cmsConfigResponsity;

    public CmsConfig getModel(String id) {
        Optional<CmsConfig> byId = cmsConfigResponsity.findById(id);
        if (byId.isPresent()){
            CmsConfig cmsConfig = byId.get();
            return cmsConfig;
        }
        return null;
    }
}
