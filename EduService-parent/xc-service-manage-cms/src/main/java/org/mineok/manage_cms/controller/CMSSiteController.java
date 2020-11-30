package org.mineok.manage_cms.controller;

import com.xuecheng.framework.model.response.QueryResponseResult;
import org.mineok.manage_cms.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Gaoming
 * @Email mineok@foxmail.com
 * @Date 2020/11/16/ 22:55
 * @Description
 */
@RestController
@RequestMapping("/cms/site")
public class CMSSiteController {
    @Autowired
    private SiteService siteService;

    // 测试案例
    @GetMapping("/list")
    public QueryResponseResult findSiteList() {
        return siteService.findAllSite();
    }

}
