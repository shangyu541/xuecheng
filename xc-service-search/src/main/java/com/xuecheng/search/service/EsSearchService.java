/**
 * FileName: EsSearchService
 * Author:   admin
 * Date:     2020/8/5 13:07
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.search.service;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author admin
 * @create 2020/8/5
 * @since 1.0.0
 */
@Service
public class EsSearchService {

    @Autowired
    RestHighLevelClient client;
    @Value("${xuecheng.elasticsearch.course.index}")
    private String es_index;
    @Value("${xuecheng.elasticsearch.course.type}")
    private String es_type;
    @Value("${xuecheng.elasticsearch.course.source_field}")
    private String source_field;

    public QueryResponseResult list(int page, int size, CourseSearchParam courseSearchParam) {
        SearchRequest searchRequest = new SearchRequest(es_index);
        searchRequest.types(es_type);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        String[] split = source_field.split(",");
        searchSourceBuilder.fetchSource(split, new String[]{});
        if (StringUtils.isNotEmpty(courseSearchParam.getKeyword())) {
            MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(courseSearchParam.getKeyword(), "name", "description", "teachplan").minimumShouldMatch("70%");
            multiMatchQueryBuilder.field("name", 10);
            boolQueryBuilder.must(multiMatchQueryBuilder);
        }
        //过滤
        if (StringUtils.isNotEmpty(courseSearchParam.getMt())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("mt",courseSearchParam.getMt()));
        }
        if (StringUtils.isNotEmpty(courseSearchParam.getSt())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("mt",courseSearchParam.getSt()));
        }
        if (StringUtils.isNotEmpty(courseSearchParam.getGrade())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("mt",courseSearchParam.getGrade()));
        }

        if (page<=0){
            page=0;
        }
        if (size<=0) {
            size=20;
        }
        int start=(page-1)*size;
        searchSourceBuilder.from(start);
        searchSourceBuilder.size(size);
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        //高亮
        highlightBuilder.preTags("<font class='eslight'>");
        highlightBuilder.postTags("</font>");
        highlightBuilder.fields().add(new HighlightBuilder.Field("name"));
        searchSourceBuilder.highlighter(highlightBuilder);

        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = null;
        try {
            search = client.search(searchRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SearchHits hits = search.getHits();
        SearchHit[] searchHits = hits.getHits();
        long totalHits = hits.getTotalHits();
        List<CoursePub> list=new ArrayList<>();
        for (SearchHit hit : searchHits
        ) {
            CoursePub coursePub=new CoursePub();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            coursePub.setName(name);
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if (highlightFields!=null){
                HighlightField highlightField = highlightFields.get("name");
                if (highlightField!=null){
                    Text[] fragments = highlightField.getFragments();
                    StringBuffer stringBuffer = new StringBuffer();
                    for (Text str:fragments
                         ) {
                        stringBuffer.append(str.string());
                    }
                    name=stringBuffer.toString();
                }
            }

            //图片
            String pic = (String) sourceAsMap.get("pic");
            coursePub.setPic(pic);
            //价格
            Float price = null;
            try {
                if (sourceAsMap.get("price") != null) {
                    price = Float.parseFloat( sourceAsMap.get("price").toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            coursePub.setPrice(price);
            Float price_old = null;
            try {
                if (sourceAsMap.get("price_old") != null) {
                    price_old = Float.parseFloat(sourceAsMap.get("price_old").toString());
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            coursePub.setPrice_old(price_old);
            list.add(coursePub);
        }
        QueryResult queryResult=new QueryResult();
        queryResult.setTotal(totalHits);
        queryResult.setList(list);
        QueryResponseResult responseResult=new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return responseResult;
    }
}
