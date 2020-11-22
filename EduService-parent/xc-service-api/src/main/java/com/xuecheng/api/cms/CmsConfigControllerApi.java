package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Author Gaoming
 * @Email mineok@foxmail.com
 * @Date 2020/11/22/ 20:05
 * @Description
 */
@Api(value = "cms配置管理接口",description = "cms配置管理接口,提供数据模型的管理,查询等")
public interface CmsConfigControllerApi {

    @ApiOperation("根据id查询cms配置信息")
    CmsConfig getModelById(String id);
}
