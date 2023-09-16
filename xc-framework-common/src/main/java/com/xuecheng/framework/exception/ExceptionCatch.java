/**
 * FileName: ExceptionCatch
 * Author:   admin
 * Date:     2020/6/16 16:53
 * Description: 异常捕获类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.framework.exception;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.google.common.collect.ImmutableMap.*;

/**
 * 〈一句话功能简述〉<br> 
 * 〈异常捕获类〉
 * @author admin
 * @create 2020/6/16
 * @since 1.0.0
 */
@RestControllerAdvice
public class ExceptionCatch {

    /**
     * 日志记录
     */
    private static  final Logger LOGGER= LoggerFactory.getLogger(ExceptionCatch.class);

    @ExceptionHandler(CustomException.class)
    public ResponseResult custonExcetion(CustomException customException){
        LOGGER.error(customException.getMessage());
        ResultCode resultCode = customException.getResultCode();
        return new ResponseResult(resultCode);
    }

    /**
     * 定义Map 配置异常类型所对应的错误代码
     */
    private static ImmutableMap<Class<? extends Throwable>,ResultCode> EXPECTIONS;

    /**
     * 定义builder对象，builder是用来构建ImmutableMap的，如何构建，放到静态代码块里面
     */
    protected static ImmutableMap.Builder<Class<? extends Throwable>,ResultCode> builder= ImmutableMap.builder();

    static {
        /**
         * 把异常的类和错误代码放到builder对象里面 ，一旦builder把数据构建到Map里面，这个map的数据将不能更改
         */
        builder.put(HttpMessageNotReadableException.class,CommonCode.INVALID_PARAM);
    }

    /**
     * 自定义map不可知异常
     * @param exception
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseResult excetion(Exception exception){
        LOGGER.error(exception.getMessage());
        if (EXPECTIONS==null){
//            EXPECTIONS构建成功
            builder.build();
        }
        /**
         * 从EXPECTIONS中获取当前传过来的exception的错误代码
         */
        ResultCode resultCode = EXPECTIONS.get(Exception.class);
        if (resultCode!=null){
            /**
             * 如果不等于null，我们就返回这个状态码
             */
            return new ResponseResult(resultCode);
        }else {
            return new ResponseResult(CommonCode.SERVER_ERROR);
        }
    }
}
