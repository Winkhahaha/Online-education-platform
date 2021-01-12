package com.xuecheng.api.filesystem;

import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import io.swagger.annotations.Api;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author Gaoming
 * @Email mineok@foxmail.com
 * @Date 2021/01/09/ 14:30
 * @Description
 */
@Api(value = "文件系统接口", description = "FastDFS构建文件系统")
public interface FileSystemControllerApi {

    UploadFileResult upload(MultipartFile multipartFile, String filetag, String businesskey, String metadata);
}
