package org.mineok.feign_client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
    public String getPayment(@PathVariable("id") Integer id);
}
