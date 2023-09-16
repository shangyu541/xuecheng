/**
 * FileName: ConsumerPostPage
 * Author:   admin
 * Date:     2020/7/14 16:22
 * Description: 监听mq
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.manage_cms_client.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xuecheng.manage_cms_client.service.CmsPageService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br> 
 * 〈监听mq〉
 * @author admin
 * @create 2020/7/14
 * @since 1.0.0
 */
@Component
public class ConsumerPostPage {

    @Autowired
    CmsPageService cmsPageService;

    @RabbitListener(queues = {"${xuecheng.mq.queue}"})
    public void postPage(String msg) throws IOException {
        /**
         * 对消息进行解析
         */
        Map map = JSON.parseObject(msg, Map.class);
        /**
         * 获取页面id
         */
        String pageId = (String) map.get("pageId");
        /**
         * 调用service方法将页面从GridFs中下载到服务器
         */
        cmsPageService.savePageToServerPage(pageId);
    }
}
