package com.xuecheng.framework.exception;

import com.google.common.collect.ImmutableMap;
import com.google.errorprone.annotations.Immutable;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author Gaoming
 * @Email mineok@foxmail.com
 * @Date 2020/11/20/ 17:28
 * @Description
 */
@ControllerAdvice   // 控制器增强
public class ExceptionCatch {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionCatch.class);

    // 定义map,配置异常类型对应的错误码
    private static ImmutableMap<Class<? extends Throwable>, ResultCode> EXCEPTIONS;
    // 定义map的builder对象,用来构建ImmutableMap
    protected static ImmutableMap.Builder<Class<? extends Throwable>, ResultCode> builder = ImmutableMap.builder();

    static {
        builder.put(HttpMessageNotReadableException.class, CommonCode.INVALID_PARAM);
    }

    // 捕获CustomException
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseResult customException(CustomException customException) {
        // 记录日志
        LOGGER.error("catch exception:{}", customException.getMessage());
        ResultCode resultCode = customException.getResultCode();
        return new ResponseResult(resultCode);
    }

    // 捕获Exception
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult OrdException(Exception exception) {
        // 记录日志
        LOGGER.error("catch exception:{}", exception.getMessage());
        if (EXCEPTIONS == null) {
            EXCEPTIONS =  builder.build();// EXCEPTIONS构建成功
        }
        // 从EXCEPTIONS中查找异常对应的错误代码,找到了响应对应错误码,否则99999
        ResultCode resultCode = EXCEPTIONS.get(exception.getClass());
        if (resultCode != null) {
            return new ResponseResult(resultCode);
        } else {
            // 返回9999异常
            return new ResponseResult(CommonCode.SERVER_ERROR);
        }
    }
}
