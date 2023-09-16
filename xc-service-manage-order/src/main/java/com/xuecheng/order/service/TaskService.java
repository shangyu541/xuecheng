/**
 * FileName: TaskService
 * Author:   admin
 * Date:     2020/8/18 16:30
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.order.service;

import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.framework.domain.task.XcTaskHis;
import com.xuecheng.order.dao.XcTaskHisRepository;
import com.xuecheng.order.dao.XcTaskRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 * @author admin
 * @create 2020/8/18
 * @since 1.0.0
 */
@Service
public class TaskService {
    @Autowired
    XcTaskRepository xcTaskRepository;

    @Autowired
    XcTaskHisRepository xcTaskHisRepository;

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 取出钱n调记录，取出指定时间之前处理的任务
     * @param updateTime
     * @param n
     * @return
     */
    public List<XcTask> findTaskList(Date updateTime, int n){
        //设置分页参数，取出前n条记录
        Pageable pageRequest = new PageRequest(0,n);
        Page<XcTask> xcTasks = xcTaskRepository.findByUpdateTimeBefore(pageRequest, updateTime);
        return xcTasks.getContent();
    }


    /**
     * 发布消息
     * @param x
     * @param mqExchange
     * @param mqRoutingkey
     */
    @Transactional
    public void publish(XcTask x, String mqExchange, String mqRoutingkey) {
        Optional<XcTask> byId = xcTaskRepository.findById(x.getId());
        if (byId.isPresent()){
            XcTask xcTask = byId.get();
            rabbitTemplate.convertAndSend(mqExchange,mqRoutingkey,xcTask);
            //更新任务时间为当前时间
            xcTask.setUpdateTime(new Date());
            xcTaskRepository.save(xcTask);
        }
    }

    /**
     * 通过乐观锁查询，看是否有相同版本和相同id的任务
     * @param id
     * @param version
     * @return
     */
    @Transactional
    public int getTask(String id, Integer version) {
        return xcTaskRepository.updateVersion(id,version);
    }

    /**
     * 删除任务，并添加到数据历史表中
     * @param id
     */
    @Transactional
    public void finishTask(String id) {
        Optional<XcTask> byId = xcTaskRepository.findById(id);
        if (byId.isPresent()){
            XcTask xcTask = byId.get();
            xcTask.setDeleteTime(new Date());
            XcTaskHis xcTaskHis=new XcTaskHis();
            BeanUtils.copyProperties(xcTask,xcTaskHis);
            xcTaskHisRepository.save(xcTaskHis);
            xcTaskRepository.delete(xcTask);
        }
    }
}
