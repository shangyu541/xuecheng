/**
 * FileName: AsyncTaskConfig
 * Author:   admin
 * Date:     2020/8/18 15:53
 * Description: task
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.order.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;

/**
 * 〈一句话功能简述〉<br> 
 * 〈定时器异步执行〉
 * @author admin
 * @create 2020/8/18
 * @since 1.0.0
 */
@Configuration
@EnableScheduling
public class AsyncTaskConfig implements SchedulingConfigurer, AsyncConfigurer {
    //线程线程池数量
    private int corePoolSize=5;

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler(){
        ThreadPoolTaskScheduler threadPoolTaskScheduler=new ThreadPoolTaskScheduler();
        //初始化线程池
        threadPoolTaskScheduler.initialize();
        //设置线程池数量
        threadPoolTaskScheduler.setPoolSize(corePoolSize);
        return threadPoolTaskScheduler;
    }


    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }

    @Override
    public Executor getAsyncExecutor() {
        Executor executor = threadPoolTaskScheduler();
        return executor;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setTaskScheduler(threadPoolTaskScheduler());
    }
}
