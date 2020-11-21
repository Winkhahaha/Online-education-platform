package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;

/**
 * @Author Gaoming
 * @Email mineok@foxmail.com
 * @Date 2020/11/20/ 17:20
 * @Description 自定义异常类型
 */
public class CustomException extends RuntimeException {

    ResultCode resultCode;

    public CustomException(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }
}
