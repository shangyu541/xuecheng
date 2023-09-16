/**
 * FileName: ExceptionCast
 * Author:   admin
 * Date:     2020/6/16 16:46
 * Description: 异常抛出类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;

/**
 * 〈一句话功能简述〉<br> 
 * 〈异常抛出类〉
 * @author admin
 * @create 2020/6/16
 * @since 1.0.0
 */
public class ExceptionCast {
    public static void cast(ResultCode resultCode){
        throw new CustomException(resultCode);
    }
}
