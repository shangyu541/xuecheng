package com.xuecheng.framework.domain.learning;

import com.xuecheng.framework.model.response.ResultCode;
import lombok.ToString;


@ToString
public enum LearningCode implements ResultCode {
    LEARNING_GETMEDIA_ERROR(false,23001,"获取学习地址失败！"),

    LEARNING_USERID_ERROR(false,23001,"用户id为null！"),

    LEARNING_XCTASK_ERROR(false,23001,"选课信息为null！");
    //操作代码
    boolean success;
    //操作代码
    int code;
    //提示信息
    String message;
    private LearningCode(boolean success, int code, String message){
        this.success = success;
        this.code = code;
        this.message = message;
    }

    @Override
    public boolean success() {
        return success;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
