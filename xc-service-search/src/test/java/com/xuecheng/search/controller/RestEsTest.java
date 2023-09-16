package com.xuecheng.search.controller;

import com.xuecheng.framework.domain.search.GsOld;
import com.xuecheng.framework.domain.search.SearchResult;
import com.xuecheng.search.dao.GsOldRepository;
import com.xuecheng.search.dao.SearchResultRepository;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


@SpringBootTest
@RunWith(SpringRunner.class)
public class RestEsTest {

    @Autowired
    RestHighLevelClient client;

    @Autowired
    RestClient restClient;

    //创建索引库
    @Test
    public void TestCreateIndex() throws IOException {
        //创建索引请求对象，并设置索引名称
        CreateIndexRequest xc_course = new CreateIndexRequest("xc_course");
        //设置索引参数
        xc_course.settings(Settings.builder().put("number_of_shards", 1)
                .put("number_of_replicas", 0));
        //设置索引mapping映射
        xc_course.mapping("doc", " {\n" + " \t\"properties\": {\n" + " \"name\": {\n" + " \"type\": \"text\",\n" + " \"analyzer\":\"ik_max_word\",\n" + " \"search_analyzer\":\"ik_smart\"\n" + " },\n" + " \"description\": {\n" + " \"type\": \"text\",\n" + " \"analyzer\":\"ik_max_word\",\n" + " \"search_analyzer\":\"ik_smart\"\n" + " },\n" + " \"studymodel\": {\n" + " \"type\": \"keyword\"\n" + " },\n" + " \"price\": {\n" + " \"type\": \"float\"\n" + " }\n" + " }\n" + "}", XContentType.JSON);
        //创建索引操作客户端
        IndicesClient indices = client.indices();
        //创建响应对象
        CreateIndexResponse createIndexResponse = indices.create(xc_course);
        //获取响应结果
        boolean shardsAcknowledged = createIndexResponse.isShardsAcknowledged();
        //输出
        System.out.println(shardsAcknowledged);
    }

    //删除索引库
    @Test
    public void TestdeleteIndex() throws IOException {
        //创建索引请求对象
        DeleteIndexRequest xc_course = new DeleteIndexRequest("xc_course");
        //创建索引操作客户端并对索引进行删除操作
        DeleteIndexResponse delete = client.indices().delete(xc_course);
        //获取响应结果
        boolean acknowledged = delete.isAcknowledged();
        System.out.println(acknowledged);
    }


    //文档添加
    @Test
    public void TestAddDoc() throws IOException {
        //定义map
        Map<Object, Object> map = new HashMap<>();
        map.put("name", "spring cloud实战");
        map.put("description", "本课程主要从四个章节进行讲解：1.微服务架构入门 2.spring cloud 基础入门 3.实战Spring Boot 4.注册中心eureka");
        map.put("studymodel", "201001");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        map.put("timestamp",simpleDateFormat.format(new Date()));
        map.put("price", 5.6f);
        //获取索引请求对象
        IndexRequest indexRequest = new IndexRequest("xc_course", "doc");
        //将map对象，放到索引中
        indexRequest.source(map);
        //执行索引文档新增操作
        IndexResponse index = client.index(indexRequest);
        //获取请求结果
        DocWriteResponse.Result result = index.getResult();
        System.out.println(result);
    }

    //查询文档
    @Test
    public void TestGetDoc() throws IOException {
        //获取索引请求对象
        GetRequest getRequest = new GetRequest(
                "xc_course",
                "doc",
                "4028e58161bcf7f40161bcf8b77c0000"
        );
        //执行请求
        GetResponse documentFields = client.get(getRequest);
        //判断是否存在
        boolean exists = documentFields.isExists();
        //转成map取出
        Map<String, Object> sourceAsMap = documentFields.getSourceAsMap();
        System.out.println(sourceAsMap);
    }

    //更新文档
    @Test
    public void updateDoc() throws IOException {
        UpdateRequest updateRequest = new UpdateRequest(
                "xc_course",
                "doc",
                "4028e58161bcf7f40161bcf8b77c0000"
        );
        Map map = new HashMap();
        map.put("name", "开发测试");
        updateRequest.doc(map);
        UpdateResponse update = client.update(updateRequest);
        RestStatus status = update.status();
        System.out.println(status);
    }

    //删除文档
    @Test
    public void deleteDoc() throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(
                "xc_course",
                "doc",
                "4028e58161bcf7f40161bcf8b77c0000"
        );
        DeleteResponse delete = client.delete(deleteRequest);
        DocWriteResponse.Result status = delete.getResult();
        System.out.println(status);
    }

    //项目搜索type类型下的文档
    @Test
    public void typeSearch() throws IOException {
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchSourceBuilder.fetchSource(new String[]{"name", "studymodel"}, new String[]{});
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = client.search(searchRequest);
        SearchHits hits = search.getHits();
        SearchHit[] hits1 = hits.getHits();
        for (SearchHit hit : hits1) {
            String index = hit.getIndex();
            String type = hit.getType();
            String id = hit.getId();
            float score = hit.getScore();
            String sourceAsString = hit.getSourceAsString();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String studymodel = (String) sourceAsMap.get("studymodel");
            String description = (String) sourceAsMap.get("description");
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(description);
        }
    }

    //分页查询
    @Test
    public void typeSearchPage() throws IOException {
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(10);
        searchSourceBuilder.fetchSource(new String[]{"name", "studymodel"}, new String[]{});
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = client.search(searchRequest);
        SearchHit[] hits = search.getHits().getHits();
        for (SearchHit hit : hits) {
            String index = hit.getIndex();
            String type = hit.getType();
            String id = hit.getId();
            float score = hit.getScore();
            String sourceAsString = hit.getSourceAsString();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String studymodel = (String) sourceAsMap.get("studymodel");
            String description = (String) sourceAsMap.get("description");
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(description);
        }
    }


    //精确查询，不会将传进来的字段值分词
    @Test
    public void typeSearchPageTerm() throws IOException {
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.termQuery("name", "spring"));
        //sour源字段过滤
        searchSourceBuilder.fetchSource(new String[]{"name", "studymodel"}, new String[]{});
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = client.search(searchRequest);
        SearchHit[] hits = search.getHits().getHits();
        for (SearchHit hit : hits) {
            String index = hit.getIndex();
            String type = hit.getType();
            String id = hit.getId();
            float score = hit.getScore();
            String sourceAsString = hit.getSourceAsString();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String studymodel = (String) sourceAsMap.get("studymodel");
            String description = (String) sourceAsMap.get("description");
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(description);
        }
    }

    //根据多个id值匹配的方法
    @Test
    public void typeSearchPageIds() throws IOException {
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        String[] ids = new String[]{"1", "2"};
        List<String> idlist = Arrays.asList(ids);
//        searchSourceBuilder.query(QueryBuilders.termsQuery("_id", idlist)); 这个可能有点问题 没实现
//        String s="1,2";
        searchSourceBuilder.query(QueryBuilders.idsQuery("doc").addIds("1").addIds("2"));
        //sour源字段过滤
        searchSourceBuilder.fetchSource(new String[]{"name", "studymodel"}, new String[]{});
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = client.search(searchRequest);
        SearchHit[] hits = search.getHits().getHits();
        for (SearchHit hit : hits) {
            String index = hit.getIndex();
            String type = hit.getType();
            String id = hit.getId();
            float score = hit.getScore();
            String sourceAsString = hit.getSourceAsString();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String studymodel = (String) sourceAsMap.get("studymodel");
            String description = (String) sourceAsMap.get("description");
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(description);
        }
    }

    /**
     * match Query即全文检索，它的搜索方式是先将搜索字符串分词，再使用各各词条从索引中搜索。
     * match query与Term query区别是match query在搜索前先将搜索关键字分词，再拿各各词语去索引中搜索
     */
    @Test
    public void typeSearchPageMatch() throws IOException {
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //sour源字段过滤
        searchSourceBuilder.fetchSource(new String[]{"name", "studymodel"}, new String[]{});
//        searchSourceBuilder.query(QueryBuilders.matchQuery("description","spring开发").operator(Operator.OR));
        /**
         * minimum_should_match 上边使用的operator = or表示只要有一个词匹配上就得分，
         * 如果实现三个词至少有两个词匹配如何实现？ 使用minimum_should_match可以指定文档匹配词的占比：
         */
        searchSourceBuilder.query(QueryBuilders.matchQuery("description", "spring开发框架").minimumShouldMatch("50%"));//设置匹配占比
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = client.search(searchRequest);
        SearchHit[] hits = search.getHits().getHits();
        for (SearchHit hit : hits) {
            String index = hit.getIndex();
            String type = hit.getType();
            String id = hit.getId();
            float score = hit.getScore();
            String sourceAsString = hit.getSourceAsString();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String studymodel = (String) sourceAsMap.get("studymodel");
            String description = (String) sourceAsMap.get("description");
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(description);
        }
    }

    /**
     * 布尔查询和muiltmatch查询合并
     *
     * @throws IOException
     */
    @Test
    public void typeSearchPageMuilth() throws IOException {
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //sour源字段过滤
        searchSourceBuilder.fetchSource(new String[]{"name", "studymodel"}, new String[]{});
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("spring框架", "description", "name").minimumShouldMatch("50%");
        multiMatchQueryBuilder.field("name", 10);
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("studymodel", "201001");
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //布尔查询
        boolQueryBuilder.must(multiMatchQueryBuilder);
        boolQueryBuilder.must(termQueryBuilder);
        //设置布尔值查询对象
        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = client.search(searchRequest);
        SearchHit[] hits = search.getHits().getHits();
        for (SearchHit hit : hits) {
            String index = hit.getIndex();
            String type = hit.getType();
            String id = hit.getId();
            float score = hit.getScore();
            String sourceAsString = hit.getSourceAsString();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String studymodel = (String) sourceAsMap.get("studymodel");
            String description = (String) sourceAsMap.get("description");
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(description);
        }
    }


    /**
     * filter过滤器
     *
     * @throws IOException
     */
    @Test
    public void typeSearchPageFilter() throws IOException {
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //sour源字段过滤
        searchSourceBuilder.fetchSource(new String[]{"name", "studymodel", "price", "description"}, new String[]{});
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("spring框架", "description", "name").minimumShouldMatch("50%");
        multiMatchQueryBuilder.field("name", 10);
        searchSourceBuilder.query(multiMatchQueryBuilder);
        //布尔查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //取上边那个拼接起来
        boolQueryBuilder.must(searchSourceBuilder.query());
        boolQueryBuilder.filter(QueryBuilders.termQuery("studymodel", "201001"));
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(60).lte(100));
        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = client.search(searchRequest);
        SearchHit[] hits = search.getHits().getHits();
        for (SearchHit hit : hits) {
            String index = hit.getIndex();
            String type = hit.getType();
            String id = hit.getId();
            float score = hit.getScore();
            String sourceAsString = hit.getSourceAsString();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String studymodel = (String) sourceAsMap.get("studymodel");
            String description = (String) sourceAsMap.get("description");
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(description);
        }
    }


    @Autowired
    GsOldRepository gsOldRepository;

    @Autowired
    SearchResultRepository searchResultRepository;

    //---------------------------------------------------------项目测试
    //甘肃项目分页查询
    @Test
    public void pnameSearchPage() throws IOException {
        List<GsOld> all = gsOldRepository.findAll();
        for (int i = 0; i < all.size(); i++) {
            SearchRequest searchRequest = new SearchRequest("t_test");
            searchRequest.types("doc");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.matchQuery("prnm", all.get(i).getOld_PRNM()).minimumShouldMatch("80%"));
//            searchSourceBuilder.query(QueryBuilders.matchQuery("prnm","海通·名郡一期项目").minimumShouldMatch("75%"));
            searchSourceBuilder.from(0);
            searchSourceBuilder.size(1);
            searchSourceBuilder.fetchSource(new String[]{"id", "prnm"}, new String[]{});
            searchRequest.source(searchSourceBuilder);
            SearchResponse search = client.search(searchRequest);
            SearchHit[] hits = search.getHits().getHits();
            for (SearchHit hit : hits) {
                String index = hit.getIndex();
                String type = hit.getType();
                String id = hit.getId();
                float score = hit.getScore();
                String sourceAsString = hit.getSourceAsString();
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                String name = (String) sourceAsMap.get("id");
                String studymodel = (String) sourceAsMap.get("prnm");
                SearchResult searchResult=new SearchResult();
                searchResult.setId(all.get(i).getOld_FID());
                searchResult.setFid(name);
                searchResult.setResultname(studymodel);
                searchResult.setSearchname(all.get(i).getOld_PRNM());
                searchResult.setScope(String.valueOf(score));
                searchResultRepository.save(searchResult);
//            System.out.println(score);
//            System.out.println(name);
//            System.out.println(studymodel);
            }
        }
    }


}