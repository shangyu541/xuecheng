package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_course.client.CmsPageClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author Administrator
 * @version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class FeignTestDao {
    @Autowired
    CmsPageClient cmsPageClient;

    @Test
    public void testRibbon(){
        //要请求的服务名
        String server="XC-SERVICE-MANAGE-CMS";
        for (int i = 0; i < 10; i++) {
            CmsPage cmsPageById = cmsPageClient.findCmsPageById("5a754adf6abb500ad05688d9");
            System.out.println(cmsPageById);
        }
    }
}
