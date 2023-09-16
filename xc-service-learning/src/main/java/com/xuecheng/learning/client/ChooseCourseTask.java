/**
 * FileName: ChooseCourseTask
 * Author:   admin
 * Date:     2020/8/20 13:11
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.learning.client;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.learning.config.RabbitMQConfig;
import com.xuecheng.learning.service.XcLearningService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author admin
 * @create 2020/8/20
 * @since 1.0.0
 */
@Component
public class ChooseCourseTask {

    @Autowired
    XcLearningService xcLearningService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = {RabbitMQConfig.XC_LEARNING_ADDCHOOSECOURSE})
    public void receiveChooseCourseTask(XcTask xcTask, Message message, Channel channel) throws ParseException {
        try {
            //通过监听的消息队列，获取接收到的选课id
            String id = xcTask.getId();
            //获取任务列表中的请求内容
            String requestBody = xcTask.getRequestBody();
            Map map = JSON.parseObject(requestBody, Map.class);
            //获取用户id
            String userId = (String) map.get("userId");
            //获取选课信息
            String courseId = (String) map.get("courseId");
            String valid = (String) map.get("valid");
            Date startTime = null;
            Date endTime = null;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //获取开始时间
            if (map.get("startTime") != null) {
                startTime = dateFormat.parse((String) map.get("startTime"));
            }
            //获取结束时间
            if (map.get("endTime") != null) {
                endTime = dateFormat.parse((String) map.get("endTime"));
            }
            //然后通过信息去查询，添加选课信息，通过任务id去任务历史表中查询，如果任务历史表中存在这个任务id，那么就返回true
            //说明这个任务已经存在资源中了，无需二次添加
            //如果没有，通过用户id和选课id进行查询选课记录，如果没有选课，则添加选课，如果有，则更新选课开始时间和结束时间（这个看需求了）
            //然后向任务历史表中添加任务，并返回true
            //然后当得到true之后，消息队列再发送信息到生产者事务
            ResponseResult result = xcLearningService.addCourse(userId, courseId, valid, startTime, endTime, xcTask);
            if (result.isSuccess()) {
                rabbitTemplate.convertAndSend(RabbitMQConfig.EX_LEARNING_ADDCHOOSECOURSE,
                        RabbitMQConfig.XC_LEARNING_FINISHADDCHOOSECOURSE_KEY, xcTask);
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            }
        }catch (Exception e){
            e.printStackTrace();
            try {
                //拒绝发送
                channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
