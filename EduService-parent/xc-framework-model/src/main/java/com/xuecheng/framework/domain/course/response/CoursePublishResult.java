package com.xuecheng.framework.domain.course.response;

import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author Gaoming
 * @Email mineok@foxmail.com
 * @Date 2021/01/16/ 17:06
 * @Description
 */
@Data
@ToString
@NoArgsConstructor
public class CoursePublishResult extends ResponseResult {

    // 页面的预览url
    String previewUrl;

    public CoursePublishResult(ResultCode resultCode, String previewUrl) {
        super(resultCode);
        this.previewUrl = previewUrl;
    }
}
