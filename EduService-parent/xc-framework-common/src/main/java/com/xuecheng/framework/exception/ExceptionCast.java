package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;

/**
 * @Author Gaoming
 * @Email mineok@foxmail.com
 * @Date 2020/11/20/ 17:25
 * @Description 异常捕捉类
 */
public class ExceptionCast {
    public static void cast(ResultCode resultCode) {
        throw new CustomException(resultCode);
    }
}
