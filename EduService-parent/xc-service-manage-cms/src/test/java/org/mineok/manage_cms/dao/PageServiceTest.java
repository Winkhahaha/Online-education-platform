package org.mineok.manage_cms.dao;

import org.mineok.manage_cms.service.PageService;
import freemarker.template.TemplateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @Author Gaoming
 * @Email mineok@foxmail.com
 * @Date 2020/11/22/ 15:04
 * @Description FreeMakerTest
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class PageServiceTest {

    @Autowired
    PageService pageService;

    @Test
    public void test_getPageHTML() throws IOException, TemplateException {
        String pageHTML = pageService.getPageHTML("5fc115fdb30f821258139068");
        System.out.println(pageHTML);
    }
}
