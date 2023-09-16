/**
 * FileName: CmsPageService
 * Author:   admin
 * Date:     2020/6/8 10:02
 * Description: 测试工程service类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.manage_cms_client.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.ResultCode;
import com.xuecheng.manage_cms_client.dao.CmsResponsity;
import com.xuecheng.manage_cms_client.dao.CmsSiteRespository;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    CmsSiteRespository cmsSiteRespository;

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    GridFSBucket gridFSBucket;

    private static final Logger LOGGER= LoggerFactory.getLogger(CmsPageService.class);

    /**
     * 保存页面到服务器的物理路径
     * @return
     */
    public void savePageToServerPage(String pageId) throws IOException {
        /**
         * 根据页面id获取页面信息
         */
        CmsPage cmsPage=this.findPageToServerPath(pageId);
        if (cmsPage==null){
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        /**
         * 从grids中查询到html文件
         */
        InputStream inputStream=this.getFileId(cmsPage.getHtmlFileId());
        if (inputStream==null){
            LOGGER.error("inputStream 是null,HtmlFileId:{}",cmsPage.getHtmlFileId());
        }
        /**
         * 获取站点信息
         */
        CmsSite cmsSite=this.findSite(cmsPage.getSiteId());
        if (cmsSite==null){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        /**
         * 获取站点的物理路径
         */
        String sitePhysicalPath = cmsSite.getSitePhysicalPath();
        /**
         * 获取页面的物理路径
         */
        String path=sitePhysicalPath+cmsPage.getPageWebPath()+cmsPage.getPageName();
        /**
         * 将html文件保存到服务器物理路径上
         */
        FileOutputStream fileOutputStream=new FileOutputStream(new File(path));
        IOUtils.copy(inputStream,fileOutputStream);
    }

    private CmsSite findSite(String siteId) {
        Optional<CmsSite> byId = cmsSiteRespository.findById(siteId);
        if (byId.isPresent()) {
            return byId.get();
        }
        return null;
    }

    private InputStream getFileId(String htmlFileId) throws IOException {
        /**
         * 获取文件对象
         */
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(htmlFileId)));
        if (gridFSFile==null){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_SAVEHTMLERROR);
        }
        /**
         * 打开下载流
         */
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        /**
         * 定义GridFsResource
         */
        GridFsResource gridFsResource = new GridFsResource(gridFSFile,gridFSDownloadStream);
        return gridFsResource.getInputStream();
    }

    private CmsPage findPageToServerPath(String pageId) {
        Optional<CmsPage> byId = cmsResponsity.findById(pageId);
        if (byId.isPresent()){
            CmsPage cmsPage = byId.get();
            return cmsPage;
        }
        return null;
    }


}
