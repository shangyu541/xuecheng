/**
 * FileName: CmsPageService
 * Author:   admin
 * Date:     2020/6/8 10:02
 * Description: 测试工程service类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.manage_cms.service;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.config.RabbitmqConfig;
import com.xuecheng.manage_cms.dao.CmsResponsity;
import com.xuecheng.manage_cms.dao.CmsTemplateResponsity;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 〈一句话功能简述〉<br>
 * 〈测试工程service类〉
 *
 * @author admin
 * @create 2020/6/8
 * @since 1.0.0
 */
@Service
public class CmsPageService {
    @Autowired
    CmsResponsity cmsResponsity;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CmsTemplateResponsity cmsTemplateResponsity;

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    GridFSBucket gridFSBucket;

    @Autowired
    RabbitTemplate rabbitTemplate;


    public QueryResponseResult findList(int page, int size, QueryPageRequest pageRequest) {
        if (pageRequest == null) {
            pageRequest = new QueryPageRequest();
        }
        if (page <= 0) {
            page = 1;
        }
        page = page - 1;
        if (size <= 0) {
            size = 10;
        }
        /**
         * 设置自定义查询条件，这里用的是模糊查询 contains
         */
        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        CmsPage cmsPage = new CmsPage();
        /**
         * 判断条件是否为null
         */
        if (StringUtils.isNotEmpty(pageRequest.getPageAliase())){
            cmsPage.setPageAliase(pageRequest.getPageAliase());
        }
        /**
         *定义Example对象
         */
        Example<CmsPage> example = Example.of(cmsPage, matcher);
        Pageable pageable = new PageRequest(page, size);
        /**
         * 将example查询条件放入find中
         */
        Page<CmsPage> all = cmsResponsity.findAll(example,pageable);
        QueryResult queryResult = new QueryResult();
        queryResult.setList(all.getContent());
        queryResult.setTotal(all.getSize());
        QueryResponseResult responseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return responseResult;
    }

    public CmsPageResult saveCmsPage(CmsPage cmsPage) {
        List<CmsPage> list = cmsResponsity.findByPageNameAndSiteIdAndPageId(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageId());
        if (!CollectionUtils.isEmpty(list)){
            ExceptionCast.cast(CommonCode.FAIL);
//            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }
        if (CollectionUtils.isEmpty(list)) {
            cmsPage.setPageId(null);
            cmsResponsity.save(cmsPage);
            return new CmsPageResult(CommonCode.SUCCESS,cmsPage);
        }
        return new CmsPageResult(CommonCode.FAIL,null);
    }

    public CmsPage findById(String id) {
        QueryResult result=new QueryResult();
        Optional<CmsPage> byId = cmsResponsity.findById(id);
        if (byId.isPresent()){
            CmsPage cmsPage = byId.get();
            return cmsPage;
        }
//        QueryResponseResult queryPageRequest=new QueryResponseResult(CommonCode.SUCCESS,QueryResult);
        return null;
    }

    public CmsPageResult updateCmsPage(String id,CmsPage cmsPage) {
        CmsPage page = this.findById(cmsPage.getPageId());
        if (page!=null) {
            page.setPageAliase(cmsPage.getPageAliase());
            return new CmsPageResult(CommonCode.SUCCESS,page);
        }
        return new CmsPageResult(CommonCode.FAIL,cmsPage);
    }

    public ResponseResult deleteById(String id) {
        Optional<CmsPage> byId = cmsResponsity.findById(id);
        if (byId.isPresent()){
            cmsResponsity.deleteById(byId.get());
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    public String getPageHtml(String pageId) throws IOException {
        /**
         * 页面静态化流程：
             * 静态化程序获取页面的DataUrl
             *
             * 静态化程序远程请求DataUrl获取数据模型
             *
             * 静态化程序获取页面的模板信息
             *
             * 执行页面静态化
         */
        /**
         * 静态化程序流程，通过pageid获取页面得信息，获取页面模板id，
         * 然后执行静态化，将页面信息执行静态化，配合对应得页面模板，生成静态化文件
         */
        /**
         * 取出页面信息
         */
        Map map=this.getModelByPageId(pageId);
        if (map==null){
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        /**
         * 获取页面模板id
         */
        String templateByPageId = this.getTemplateByPageId(pageId);
        if (StringUtils.isEmpty(templateByPageId)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        /**
         * 执行页面静态化
         */
        String html = this.generateHtml(templateByPageId, map);
        return html;
    }

    private String generateHtml(String templateByPageId, Map map) throws IOException {
        /**
         * 查询模板信息
         */
        Optional<CmsTemplate> byId = cmsTemplateResponsity.findById(templateByPageId);
        if (byId.isPresent()){
            CmsTemplate cmsTemplate = byId.get();
            /**
             * 获取模板id
             */
            String templateFileId = cmsTemplate.getTemplateFileId();
            /**
             * 条件查询，把文件查询出来
             */
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
            /**
             * 打开一个下载流对象
             */
            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            /**
             *创建GridFsResource对象，获取流
             */
            GridFsResource gridFsResource = new GridFsResource(gridFSFile,gridFSDownloadStream);
            /**
             * io流输出，从流中获取数据
             */
            String s = IOUtils.toString(gridFsResource.getInputStream(), StandardCharsets.UTF_8);
//            System.out.println(s);
            return s;
        }
        return null;
    }


    private String getTemplateByPageId(String pageId) {

        /**
         * 根据页面id获取页面信息
         */
        Optional<CmsPage> byId = cmsResponsity.findById(pageId);
        if (byId.isPresent()) {
            CmsPage cmsPage = byId.get();
            if (cmsPage == null) {
                ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
            }
            /**
             * 获取页面的模板id
             */
            String templateId = cmsPage.getTemplateId();
            if (StringUtils.isEmpty(templateId)) {
                ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
            }
            /**
             * 通过resttemplate请求dataurl获取数据
             */
            return templateId;
        }
        return null;
    }

    private Map getModelByPageId(String pageId) {
        /**
         * 根据页面id获取页面信息
         */
        Optional<CmsPage> byId = cmsResponsity.findById(pageId);
        if (byId.isPresent()){
            CmsPage cmsPage = byId.get();
            if (cmsPage==null){
                ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
            }
            String dataUrl = cmsPage.getDataUrl();
            if (StringUtils.isEmpty(dataUrl)){
                ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
            }
            /**
             * 通过resttemplate请求dataurl获取数据
             */
            ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
            Map body = forEntity.getBody();
            return body;
        }

        return null;
    }

    public ResponseResult post(String pageId) throws IOException {
        /**
         * 执行静态化，
         * 将页面静态化文件存储到gridFs中
         * 向mq发送消息
         */
        //执行页面静态化
        String pageHtml = this.getPageHtml(pageId);
        //将页面静态化文件存储到Grids中
        CmsPage cmsPage=saveHtml(pageId,pageHtml);
        sendPostPage(cmsPage.getPageId());
        return new ResponseResult(CommonCode.SUCCESS);
    }

    private void sendPostPage(String pageId) {
        Optional<CmsPage> byId = cmsResponsity.findById(pageId);
        if (!byId.isPresent()){
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        CmsPage cmsPage = byId.get();
        Map<Object, Object> map = new HashMap<>();
        map.put("pageId",pageId);
        String s = JSON.toJSONString(map);
        String siteId = cmsPage.getSiteId();
        rabbitTemplate.convertAndSend(RabbitmqConfig.EX_ROUTING_CMS_POSTAGE,siteId,s);
    }

    private CmsPage saveHtml(String pageId, String content) throws IOException {
        Optional<CmsPage> byId = cmsResponsity.findById(pageId);
        if (!byId.isPresent()){
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        CmsPage cmsPage = byId.get();
        //存储之前先删除
        String htmlFileId = cmsPage.getHtmlFileId();
        if(StringUtils.isNotEmpty(htmlFileId)){
            gridFsTemplate.delete(Query.query(Criteria.where("_id").is(htmlFileId)));
        }
        //保存html文件到GridFs
        InputStream inputStream = IOUtils.toInputStream(content,"UTF-8");
        ObjectId store = gridFsTemplate.store(inputStream, cmsPage.getPageName());
        //文件id
        String fileId = store.toString();
        cmsPage.setHtmlFileId(fileId);
        //替换文件id
        cmsResponsity.save(cmsPage);
        return cmsPage;
    }
}
