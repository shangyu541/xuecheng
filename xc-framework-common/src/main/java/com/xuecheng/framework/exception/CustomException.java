/**
 * FileName: CustomException
 * Author:   admin
 * Date:     2020/6/16 16:39
 * Description: 自定义异常类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;

/**
 * 〈一句话功能简述〉<br> 
 * 〈自定义异常类〉
 * @author admin
 * @create 2020/6/16
 * @since 1.0.0
 */
public class CustomException extends RuntimeException {

    ResultCode resultCode;

    public CustomException(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

    public void setResultCode(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

}
