package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;

/**
 * @Author Gaoming
 * @Email mineok@foxmail.com
 * @Date 2020/11/16/ 16:45
 * @Description CMS页面查询接口
 */
public interface CmsPageControllerApi {
    // 页面查询
    QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);
}
