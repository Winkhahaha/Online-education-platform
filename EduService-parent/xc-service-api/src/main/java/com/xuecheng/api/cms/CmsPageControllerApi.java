package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @Author Gaoming
 * @Email mineok@foxmail.com
 * @Date 2020/11/16/ 16:45
 * @Description CMS页面接口
 */
@Api(value = "cms页面管理接口", description = "cms页面管理接口，提供页面的增、删、改、查")
public interface CmsPageControllerApi {
    // 页面查询
    @ApiOperation("分页查询页面列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, paramType = "path", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页记录数", required = true, paramType = "path", dataType = "int")
    })
    QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);

    // 新增页面
    @ApiOperation("新增页面")
    CmsPageResult add(CmsPage cmsPage);

    // 根据pageId查询页面
    @ApiOperation("根据pageId查询页面")
    CmsPage findByPageId(String pageId);

    // 修改指定页面
    @ApiOperation("修改指定页面")
    CmsPageResult editPage(String pageId, CmsPage cmsPage);

    @ApiOperation("刪除頁面")
    ResponseResult delete(String pageId);

    @ApiOperation("页面发布")
    ResponseResult post(String pageId);

    // 保存页面:有更新,无添加
    @ApiOperation("新增页面")
    CmsPageResult save(CmsPage cmsPage);
}
