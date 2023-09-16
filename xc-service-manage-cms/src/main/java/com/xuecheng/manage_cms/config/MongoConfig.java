/**
 * FileName: MongoConfig
 * Author:   admin
 * Date:     2020/6/22 20:13
 * Description: mongodb配置类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.manage_cms.config;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 〈一句话功能简述〉<br> 
 * 〈mongodb配置类〉
 * @author admin
 * @create 2020/6/22
 * @since 1.0.0
 */
@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.database}")
    String db;

    @Bean
    public GridFSBucket getGridFsTemplate(MongoClient mongoClient){
        MongoDatabase database = mongoClient.getDatabase(db);
        GridFSBucket gridFSBucket = GridFSBuckets.create(database);
        return gridFSBucket;
    }

}
