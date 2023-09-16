package com.xuecheng.manage_cms.controller;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_cms.dao.CmsResponsity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageControllerTest {
    @Autowired
    CmsResponsity cmsResponsity;

    @Test
    public void findList() {
        CmsPage cmsPage = new CmsPage();
        cmsPage.setPageAliase("首");
        ExampleMatcher matcher = ExampleMatcher.matching();
        matcher = matcher.withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        Example<CmsPage> example = Example.of(cmsPage, matcher);
        List<CmsPage> all = cmsResponsity.findAll(example);
        System.out.println(all);
    }
}