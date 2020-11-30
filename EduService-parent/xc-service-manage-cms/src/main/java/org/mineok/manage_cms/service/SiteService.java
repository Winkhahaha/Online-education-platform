package org.mineok.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import org.mineok.manage_cms.dao.CMSSiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author Gaoming
 * @Email mineok@foxmail.com
 * @Date 2020/11/18/ 21:20
 * @Description
 */
@Service
public class SiteService {

    @Autowired
    private CMSSiteRepository cmsSiteRepository;

    // 查询所有站点对象集合,CMSPage页面查询时下拉显示
    public QueryResponseResult findAllSite() {
        // 创建数据结果集
        QueryResult<CmsSite> queryResult = new QueryResult<>();
        queryResult.setList(cmsSiteRepository.findAll());
        // 返回响应结果集
        return new QueryResponseResult(CommonCode.SUCCESS, queryResult);
    }
}

