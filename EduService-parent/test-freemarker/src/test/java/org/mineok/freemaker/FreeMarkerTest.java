package org.mineok.freemaker;

import com.xuecheng.test.freemarker.FreemarkerTestApplication;
import com.xuecheng.test.freemarker.model.Student;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @Author Gaoming
 * @Email mineok@foxmail.com
 * @Date 2020/11/22/ 15:04
 * @Description FreeMakerTest
 */
@SpringBootTest(classes = FreemarkerTestApplication.class)
@RunWith(SpringRunner.class)
public class FreeMarkerTest {

    @Test  // 测试Freemaker静态化,基于ftl模板文件生成html文件
    public void test_GenerateHTML() throws IOException, TemplateException {
        // 获取存放模板文件的文件夹路径
        String classpath = this.getClass().getResource("/").getPath() + "/templates/";
        // 定义配置类
        Configuration configuration = new Configuration(Configuration.getVersion());
        // 配置类设置存放模板文件的文件夹
        configuration.setDirectoryForTemplateLoading(new File(classpath));
        // 设置指定使用的模板文件名,获取模板对象
        Template template = configuration.getTemplate("test1.ftl");
        /*
        抽取: getTemplateByftlFile(String ftlFilesDirPath,String ftlFileName);
        Template template = getTemplateByftlFile(classpath,"test1.ftl");
         */
        // 获取数据模型
        Map map = getDatasByMap();
        toHTMLByTemplate(template, map, "E:\\CodeRepositories\\Online-education-platform\\EduService-parent\\test-freemarker\\StaticFilesDirectory\\test3.html");
    }


    @Test  // 基于模板文件的内容(字符串)生成html文件
    public void test_GenerateHTML_By_String() throws IOException, TemplateException {
        //设置模板内容(字符串),测试时使用简单的字符串作为模板(templateStr)
        String templateString = "<html>\n" +
                "    <head>\n" +
                "        <meta charset=\"utf-8\">\n" +
                "        <title>test02</title>\n" +
                "    </head>\n" +
                "    <body>\n" +
                "    名称：${name}\n" +
                "    </body>\n" +
                "</html>";
        // 定义配置类
        Configuration configuration = new Configuration(Configuration.getVersion());
        // 使用模板加载器将模板字符串变成模板,name为模板名称(targetTemplateName)
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template", templateString);
        // 设置配置类的模板加载器
        configuration.setTemplateLoader(stringTemplateLoader);
        // 获取模板(根据模板加载器设置的name)
        Template template = configuration.getTemplate("template", "utf-8");
        /*
        抽取: getTemplateByString(String templateStr,String targetTemplateName,String encoding);
        Template template = getTemplateByString(templateString,"template","utf-8");
         */
        // 定义数据模型
        Map map = getDatasByMap();
        // 静态化
        toHTMLByTemplate(template, map, "E:\\CodeRepositories\\Online-education-platform\\EduService-parent\\test-freemarker\\StaticFilesDirectory\\test2.html");
    }

    // Freemaker静态化:Template生成HTML文件
    private void toHTMLByTemplate(Template template, Object datas, String targetPath) throws IOException, TemplateException {
        // 静态化
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, datas);
        // System.out.println(content);
        InputStream inputStream = IOUtils.toInputStream(content);
        FileOutputStream outputStream = new FileOutputStream(new File(targetPath));
        // 输出文件
        IOUtils.copy(inputStream, outputStream);
        // 关流
        inputStream.close();
        outputStream.close();
    }

    // 获取自定义数据模型
    public Map getDatasByMap() {
        Map map = new HashMap<>();
        map.put("name", "哈哈哈");
        Student stu1 = new Student();
        stu1.setName("小明");
        stu1.setAge(18);
        stu1.setMoney(1000.86f);
        stu1.setBirthday(new Date());
        Student stu2 = new Student();
        stu2.setName("小红");
        stu2.setMoney(200.1f);
        stu2.setAge(19);
        stu2.setBirthday(new Date());
        List<Student> friends = new ArrayList<>();
        friends.add(stu1);
        stu2.setFriends(friends);
        stu2.setBestFriend(stu1);
        List<Student> stus = new ArrayList<>();
        stus.add(stu1);
        stus.add(stu2);
        //向数据模型放数据
        map.put("stus", stus);
        //准备map数据
        HashMap<String, Student> stuMap = new HashMap<>();
        stuMap.put("stu1", stu1);
        stuMap.put("stu2", stu2);
        //向数据模型放数据
        map.put("stu1", stu1);
        //向数据模型放数据
        map.put("stuMap", stuMap);
        map.put("point", 102920122);
        // 返回freemarker模板的位置:基于resources\templates目录下 -- test1.ftl
        return map;
    }
}
