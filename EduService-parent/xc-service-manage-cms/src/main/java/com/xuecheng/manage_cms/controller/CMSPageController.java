package com.xuecheng.manage_cms.controller;

import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.manage_cms.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Gaoming
 * @Email mineok@foxmail.com
 * @Date 2020/11/16/ 22:55
 * @Description
 */
@RestController
@RequestMapping("/cms/page")
public class CMSPageController {
    @Autowired
    private PageService pageService;

    // 测试案例
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult findList(@PathVariable("page") int page, @PathVariable("size") int size, QueryPageRequest queryPageRequest) {

/*        //暂时用静态数据
        //定义queryResult
        QueryResult<CmsPage> queryResult =new QueryResult<>();
        List<CmsPage> list = new ArrayList<>();
        CmsPage cmsPage = new CmsPage();
        cmsPage.setPageName("测试页面");
        list.add(cmsPage);
        queryResult.setList(list);
        queryResult.setTotal(1);

        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return queryResponseResult;*/
        // 调用service演示查询mongdb
        return pageService.findList(page, size, queryPageRequest);
    }

}
