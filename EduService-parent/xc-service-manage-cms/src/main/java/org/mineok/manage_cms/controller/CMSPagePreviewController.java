package org.mineok.manage_cms.controller;

import com.xuecheng.framework.web.BaseController;
import org.mineok.manage_cms.service.PageService;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletOutputStream;
import java.io.IOException;

/**
 * @Author Gaoming
 * @Email mineok@foxmail.com
 * @Date 2020/11/23/ 16:09
 * @Description
 */
@Controller
public class CMSPagePreviewController extends BaseController {

    @Autowired
    private PageService pageService;

    // 页面预览
    @RequestMapping(value = "/cms/preview/{pageId}", method = RequestMethod.GET)
    public void preview(@PathVariable("pageId") String pageId) throws IOException, TemplateException {
        // 执行静态化
        String pageHTML = pageService.getPageHTML(pageId);
        // 通过response对象将内容输出
        ServletOutputStream outputStream = response.getOutputStream();
        response.setHeader("Content-type", "text/html;charset=utf-8");
        outputStream.write(pageHTML.getBytes("utf-8"));
    }
}
