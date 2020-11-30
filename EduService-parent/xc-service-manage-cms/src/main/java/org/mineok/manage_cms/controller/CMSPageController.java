package org.mineok.manage_cms.controller;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import org.mineok.manage_cms.service.PageService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author Gaoming
 * @Email mineok@foxmail.com
 * @Date 2020/11/16/ 22:55
 * @Description
 */
@RestController
@RequestMapping("/cms/page")
public class CMSPageController implements CmsPageControllerApi {
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

    @Override
    @PostMapping("/add")
    public CmsPageResult add(@RequestBody CmsPage cmsPage) {
        return pageService.addPage(cmsPage);
    }

    @Override
    @GetMapping("/get/{pageId}")
    public CmsPage findByPageId(@PathVariable("pageId") String pageId) {
        return pageService.findByPageId(pageId);
    }

    @Override
    @PutMapping("/edit/{pageId}")
    public CmsPageResult editPage(@PathVariable("pageId") String pageId, @RequestBody CmsPage cmsPage) {
        return pageService.updatePage(pageId, cmsPage);
    }

    @Override
    @DeleteMapping("/del/{pageId}")
    public ResponseResult delete(@PathVariable("pageId") String pageId) {
        return pageService.deletePageByPageId(pageId);
    }

    @SneakyThrows
    @Override
    @PostMapping("/postPage/{pageId}")
    public ResponseResult post(@PathVariable("pageId") String pageId) {
        return pageService.post(pageId);
    }
}
