package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.CustomException;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CMSConfigRepository;
import com.xuecheng.manage_cms.dao.CMSPageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

/**
 * @Author Gaoming
 * @Email mineok@foxmail.com
 * @Date 2020/11/16/ 22:53
 * @Description
 */
@Service
public class PageService {

    @Autowired
    private CMSPageRepository cmsPageRepository;

    @Autowired
    private CMSConfigRepository cmsConfigRepository;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 页面查询方法
     *
     * @param page             页码，从1开始记数
     * @param size             每页记录数
     * @param queryPageRequest 查询条件
     * @return
     */
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        if (queryPageRequest == null) {
            queryPageRequest = new QueryPageRequest();
        }
        // 条件值对象
        CmsPage cmsPage = new CmsPage();
        // 设置站点ID
        if (!StringUtils.isEmpty(queryPageRequest.getSiteId())) {
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        // 模板ID
        if (!StringUtils.isEmpty(queryPageRequest.getTemplateId())) {
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        // 站点别名
        if (!StringUtils.isEmpty(queryPageRequest.getPageAliase())) {
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        // 自定义条件查询
        // 定义条件匹配器(根据参数对其模糊查询)
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().
                // ExampleMatcher.GenericPropertyMatchers.contains() 包含关键字
                        withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        // 定义条件对象
        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);
        // 定义分页
        Pageable pageable = PageRequest.of(page - 1, size);
        // 实现自定义条件查询并且分页
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);
        // 定义结果集
        QueryResult queryResult = new QueryResult();
        queryResult.setList(all.getContent());//数据列表
        queryResult.setTotal(all.getTotalElements());//数据总记录数
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return queryResponseResult;
    }

    // 新增页面
//    public CmsPageResult addPage(CmsPage cmsPage) {
//        // 校验页面名称/站点ID/页面webPath的唯一性
//        // 根据页面名称/站点ID/页面webPath去cms_page集合,如果查到说明此页面已经存在,不存在则新增
//        if (cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath()) == null) {
//            // 数据库会自增ID
//            cmsPage.setPageId(null);
//            // 调用dao新增页面
//            cmsPageRepository.save(cmsPage);
//            return new CmsPageResult(CommonCode.SUCCESS, cmsPage);
//        }
//        // 添加失败
//        return new CmsPageResult(CommonCode.FAIL, null);
//    }
    public CmsPageResult addPage(CmsPage cmsPage) {
        if (cmsPage == null) {
            // 参数为空,抛出异常
        }
        // 校验页面名称/站点ID/页面webPath的唯一性
        // 根据页面名称/站点ID/页面webPath去cms_page集合,如果查到说明此页面已经存在,不存在则新增
        if (cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath()) != null) {
            // 页面已经存在
            // 抛出异常:页面已经存在
//            throw new CustomException(CommonCode.FAIL);
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }
        // 数据库会自增ID
        cmsPage.setPageId(null);
        // 调用dao新增页面
        cmsPageRepository.save(cmsPage);
        return new CmsPageResult(CommonCode.SUCCESS, cmsPage);
    }

    // 根据pageId查询指定页面
    public CmsPage findByPageId(String pageId) {
        Optional<CmsPage> byId = cmsPageRepository.findById(pageId);
        if (byId.isPresent()) {
            return byId.get();
        }
        return null;
    }

    // 修改指定页面
    public CmsPageResult updatePage(String pageId, CmsPage newPage) {
        // 根据pageId查询指定页面
        CmsPage oldPage = findByPageId(pageId);
        if (oldPage != null) {
            // 存在则修改oldPage
            //更新模板id
            oldPage.setTemplateId(newPage.getTemplateId());
            //更新所属站点
            oldPage.setSiteId(newPage.getSiteId());
            //更新页面别名
            oldPage.setPageAliase(newPage.getPageAliase());
            //更新页面名称
            oldPage.setPageName(newPage.getPageName());
            //更新访问路径
            oldPage.setPageWebPath(newPage.getPageWebPath());
            //更新物理路径
            oldPage.setPagePhysicalPath(newPage.getPagePhysicalPath());
            // 提交修改
            cmsPageRepository.save(oldPage);
            return new CmsPageResult(CommonCode.SUCCESS, oldPage);
        }
        return new CmsPageResult(CommonCode.FAIL, null);
    }

    // 根据pageId删除页面
    public ResponseResult deletePageByPageId(String pageId) {
        if (cmsPageRepository.findById(pageId).isPresent()) {
            cmsPageRepository.deleteById(pageId);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    // 根据Id查询cmsConfig
    public CmsConfig getConfigById(String Id) {
        Optional<CmsConfig> byId = cmsConfigRepository.findById(Id);
        if (byId.isPresent()) {
            return byId.get();
        }
        return null;
    }
}
