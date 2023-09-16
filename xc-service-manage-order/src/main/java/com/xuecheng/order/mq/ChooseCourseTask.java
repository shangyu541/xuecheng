/**
 * FileName: ChooseCourseTask
 * Author:   admin
 * Date:     2020/8/18 15:15
 * Description: spring定时器 串行任务
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.order.mq;

import com.rabbitmq.client.Channel;
import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.order.config.RabbitMQConfig;
import com.xuecheng.order.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈spring定时器 串行任务〉
 *
 * @author admin
 * @create 2020/8/18
 * @since 1.0.0
 */
@Component
@EnableScheduling
public class ChooseCourseTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChooseCourseTask.class);

    //定义任务调度策略 每隔三秒执行一次
//    @Scheduled(cron="0/3 * * * * *")
//    @Scheduled(fixedRate = 3000) //在任务开始后3s执行下一次调度
//    @Scheduled(fixedDelay = 3000)//  在任务结束后3s后才开始执行
    public void task() {
        LOGGER.info("========定时任务开启========");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.info("========定时任务关闭========");
    }

    //    @Scheduled(fixedRate = 3000)
    public void task2() {
        LOGGER.info("========定时任务开启2========");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.info("========定时任务关闭2========");
    }

    @Autowired
    TaskService taskService;

    @Scheduled(fixedDelay = 60000)
    public void sendCourseTask() {
        //取出当前时间一分钟之前的时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.set(GregorianCalendar.MINUTE, -1);
        Date time = calendar.getTime();
        //查询当前时间一分钟之前的时间，然后去查询这个时间点之前的任务list
        List<XcTask> taskList = taskService.findTaskList(time, 1000);
        //如果list有值
        if (!CollectionUtils.isEmpty(taskList)){
            for (XcTask x:taskList
                 ) {
                //调用乐观锁，查看程序是否可以执行,
                // (乐观锁机制，假设这个版本值别人不会动他，只有我在操作，我对这个(相同任务id，相同版本号)版本值进行+1)
                //当返回值大于0了 说明这个值改变了，防止重复修改数据，保持了数据的一致性
                if (taskService.getTask(x.getId(),x.getVersion())>0)
                {
                    //发布消息，通过任务id查询出任务，如果任务还存在
                    // 那么通过消息队列，告诉事务的另一方，我这里操作成功了，同时更新任务表里面的时间为当前时间
                    taskService.publish(x,x.getMqExchange(),x.getMqRoutingkey());
                }
            }
        }
    }

    /**
     * 当获取到消息id之后，将数据消除，并且将数据备份到历史数据库,如果这里没有成功，他就会重复之前的步骤，直到成功
     * @param xcTask
     * @param message
     * @param channel
     */
    @RabbitListener(queues = RabbitMQConfig.XC_LEARNING_FINISHADDCHOOSECOURSE)
    public void reviceFinishChooseTask(XcTask xcTask, Message message, Channel channel){
        String id = xcTask.getId();
        taskService.finishTask(id);
    }
}
