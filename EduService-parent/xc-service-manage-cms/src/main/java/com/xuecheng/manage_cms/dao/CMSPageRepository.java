package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Author Gaoming
 * @Email mineok@foxmail.com
 * @Date 2020/11/16/ 22:52
 * @Description
 */
public interface CMSPageRepository extends MongoRepository<CmsPage,String> {

    // 根据页面名称查询(演示案例)
    CmsPage findByPageName(String pageName);

    // 根据页面名称/站点ID/页面webPath查询
    CmsPage findByPageNameAndSiteIdAndPageWebPath(String pageName,String siteId,String pageWebPath);
}
