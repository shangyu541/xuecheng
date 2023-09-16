/**
 * FileName: SearchController
 * Author:   admin
 * Date:     2020/8/5 13:04
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.search.controller;

import com.xuecheng.api.cms.EsCourseControllerApi;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.search.service.EsSearchService;
import org.elasticsearch.search.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 * @author admin
 * @create 2020/8/5
 * @since 1.0.0
 */
@RestController
@RequestMapping("/search/course")
public class SearchController implements EsCourseControllerApi {

    @Autowired
    EsSearchService esSearchService;

    @GetMapping("/list/{page}/{size}")
    @Override
    public QueryResponseResult list(@PathVariable("page") int page, @PathVariable("size") int size, CourseSearchParam courseSearchParam) {
        return esSearchService.list(page,size,courseSearchParam);
    }
}
