/**
 * FileName: GridFsControllerApi
 * Author:   admin
 * Date:     2020/6/21 17:37
 * Description: GridFs
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.manage_cms.controller;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 〈一句话功能简述〉<br> 
 * 〈GridFs〉
 * @author admin
 * @create 2020/6/21
 * @since 1.0.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class GridFsControllerApi {

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    GridFSBucket gridFSBucket;

    @Test
    public void testGridFsController() throws FileNotFoundException {
        File file=new File("D:\\index_banner.ftl");
        FileInputStream fileInputStream=new FileInputStream(file);
        ObjectId store = gridFsTemplate.store(fileInputStream, "index_banner.ftl");
        System.out.println(store);

//        5ef01038d406352c6cbf92b0
    }

    @Test
    public void queryFile() throws IOException {
        /**
         * 条件查询，把文件查询出来
         */
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is("5ef01038d406352c6cbf92b0")));
        /**
         * 打开一个下载留对象
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
        System.out.println(s);
    }
}
