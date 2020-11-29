package org.mineok.manage_cms_client.mq;

import com.alibaba.fastjson.JSON;
import org.mineok.manage_cms_client.service.PageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @Author Gaoming
 * @Email mineok@foxmail.com
 * @Date 2020/11/28/ 20:19
 * @Description 监听MQ, 接收页面发布消息
 */
@Component
public class ConsumerPostPage {

    private static final Logger LOGGER = LoggerFactory.getLogger(PageService.class);

    @Autowired
    PageService pageService;

    @RabbitListener(queues = {"${test.mq.queue}"})
    public void postPage(String msg) throws IOException {
        // 解析消息
        Map map = JSON.parseObject(msg, Map.class);
        // 得到消息当中的pageId
        String pageId = (String) map.get("pageId");
        // 判断pageId是否合法
        if (pageService.findPageByPageId(pageId) == null) {
            LOGGER.error("findPageByPageId return null , pageId:{}", pageId);
            return;
        }
        // 调用service方法将页面从GridFS下载到服务器
        pageService.savePageToServerPath(pageId);
    }
}
