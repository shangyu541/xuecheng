/**
 * FileName: CostomExceptionCatch
 * Author:   admin
 * Date:     2020/8/14 15:27
 * Description: 定义异常处理类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.manage_course.exception;

import com.xuecheng.framework.exception.ExceptionCatch;
import com.xuecheng.framework.model.response.CommonCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * 〈一句话功能简述〉<br> 
 * 〈定义异常处理类〉
 * @author admin
 * @create 2020/8/14
 * @since 1.0.0
 */
@ControllerAdvice
public class CustomExceptionCatch extends ExceptionCatch {

//    public static final Logger LOGGER= LoggerFactory.getLogger(ExceptionCatch.class);

    static {
        //除了CustomException以外的异常类型及对应的错误代码在这里定义，如果不定义则统一返回固定的错误信息
        builder.put(AccessDeniedException.class, CommonCode.UNAUTHORISE);
    }

}
