package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Author Gaoming
 * @Email mineok@foxmail.com
 * @Date 2020/11/22/ 20:08
 * @Description
 */
public interface CMSConfigRepository extends MongoRepository<CmsConfig, String> {
}
