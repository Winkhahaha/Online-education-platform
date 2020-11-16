package com.xuecheng.framework.domain.cms.request;

import lombok.Data;

/**
 * @Author Gaoming
 * @Email mineok@foxmail.com
 * @Date 2020/11/16/ 16:40
 * @Description
 * 1、分页查询CmsPage 集合下的数据
 * 2、根据站点Id、模板Id、页面别名查询页面信息
 * 3、接口基于Http Get请求，响应Json数据
 */
@Data
public class QueryPageRequest {
    // 接收页面查询条件
    //站点id
    private String siteId;
    //页面ID
    private String pageId;
    //页面名称
    private String pageName;
    //别名
    private String pageAliase;
    //模版id
    private String templateId;
}
