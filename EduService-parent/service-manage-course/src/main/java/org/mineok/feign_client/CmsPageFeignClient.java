package org.mineok.feign_client;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author Gaoming
 * @Email mineok@foxmail.com
 * @Date 2021/01/15/ 16:41
 * @Description
 */
@FeignClient("xc-service-manage-cms")
@Component
public interface CmsPageFeignClient {

    @GetMapping(value = "/cms/page/nacos/{id}")
    String getPayment(@PathVariable("id") Integer id);

    // 添加页面,用于课程预览
    @PostMapping("/cms/page/save")
    CmsPageResult saveCmsPage(@RequestBody CmsPage cmsPage);
}
