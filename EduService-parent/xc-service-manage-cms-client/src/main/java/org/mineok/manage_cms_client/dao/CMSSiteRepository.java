package org.mineok.manage_cms_client.dao;

import com.xuecheng.framework.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Author Gaoming
 * @Email mineok@foxmail.com
 * @Date 2020/11/18/ 21:14
 * @Description 门站
 */
public interface CMSSiteRepository extends MongoRepository<CmsSite,String> {
}
