/**
 * FileName: ElasticSearchConfig
 * Author:   admin
 * Date:     2020/8/3 14:02
 * Description: es配置类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;

/**
 * 〈一句话功能简述〉<br> 
 * 〈es配置类〉
 * @author admin
 * @create 2020/8/3
 * @since 1.0.0
 */
@Configuration
public class ElasticSearchConfig {

    /**
     * 获取集群节点
     */
    @Value("${xuecheng.elasticsearch.hostlist}")
    private String hostlist;

    /**
     * 定义两个bean 其实是两种api操作方式 ，用那种都行
     *
     * @return
     */
    @Bean
    public RestHighLevelClient restHighLevelClient(){
        //对集群节点进行切割处理
        String[] eslist = hostlist.split(",");
        //声明httpHost
        HttpHost[] httpHosts=new HttpHost[eslist.length];
        for (int i = 0; i <eslist.length ; i++) {
            String item=eslist[i];
            //将遍历的集群节点放入httpHost中
            httpHosts[i] = new HttpHost(item.split(":")[0], Integer.parseInt(item.split(":")[1]), "http");
        }
        //创建RestHighLevelClient客户端
        return new RestHighLevelClient(RestClient.builder(httpHosts));
    }
    /**
     * 低级客户端同理，但是暂时先不用
     */
    @Bean
    public RestClient restClient(){
        String[] eslist = hostlist.split(",");
        HttpHost[] httpHosts=new HttpHost[eslist.length];
        for (int i = 0; i <eslist.length ; i++) {
            String item=eslist[i];
            httpHosts[i] = new HttpHost(item.split(":")[0], Integer.parseInt(item.split(":")[1]), "http");
        }
        return RestClient.builder(httpHosts).build();
    }


}
