package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-09-12 18:11
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest {

    @Autowired
    CMSPageRepository cmsPageRepository;

    @Test
    public void testFindAll() {
        List<CmsPage> all = cmsPageRepository.findAll();
        System.out.println(all);

    }

    //分页查询
    @Test
    public void testFindPage() {
        //分页参数
        int page = 1;//从0开始
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        Page<CmsPage> all = cmsPageRepository.findAll(pageable);
        System.out.println(all);
    }

    // 自定义条件查询
    @Test
    public void testFindAllByExample() {
        //分页参数
        int page = 1;//从0开始
        int size = 10;
        Pageable pageable = PageRequest.of(page - 1, size);
        // 条件值对象(精确匹配)
        CmsPage cmsPage = new CmsPage();
//        cmsPage.setSiteId("5a751fab6abb5044e0d19ea1");
        cmsPage.setPageAliase("轮播图");
        // 条件匹配器(可进行模糊查询)
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().
                // ExampleMatcher.GenericPropertyMatchers.contains() 包含关键字
                        withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
//        exampleMatcher = exampleMatcher.withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        // 定义Example
        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);
        System.out.println(all.getContent());
    }

    //修改
    @Test
    public void testUpdate() {
        //查询对象
        Optional<CmsPage> optional = cmsPageRepository.findById("5b4b1d8bf73c6623b03f8cec");
        if (optional.isPresent()) {
            CmsPage cmsPage = optional.get();
            //设置要修改值
            cmsPage.setPageAliase("test01");
            //...
            //修改
            CmsPage save = cmsPageRepository.save(cmsPage);
            System.out.println(save);
        }

    }

    //根据页面名称查询
    @Test
    public void testfindByPageName() {
        CmsPage cmsPage = cmsPageRepository.findByPageName("测试页面");
        System.out.println(cmsPage);
    }

    @Autowired
    CMSSiteRepository cmsSiteRepository;

    // 查询站点对象集合,CMSPage页面查询时下拉显示
    @Test
    public void testfindSite() {
        List<CmsSite> all = cmsSiteRepository.findAll();
        System.out.println(all);
    }

    @Test
    public void testfindPageAndSite() {
        List<CmsPage> all = cmsPageRepository.findAll();
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < all.size(); i++) {
            CmsSite cmsSite = cmsSiteRepository.findById(all.get(i).getSiteId()).get();
            map.put(all.get(i).getPageAliase(), cmsSite.getSiteName());
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + "~~~" + entry.getValue());
        }
    }
}
