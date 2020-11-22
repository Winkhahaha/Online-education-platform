package org.mineok.freemaker;

import com.xuecheng.test.freemarker.FreemarkerTestApplication;
import com.xuecheng.test.freemarker.model.Student;
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
        // 定义配置类
        Configuration configuration = new Configuration(Configuration.getVersion());
        // 获取模板文件所在文件夹路径
        String classpath = this.getClass().getResource("/").getPath();
        // System.out.println(path);
        // 设置存放模板文件的文件夹
        configuration.setDirectoryForTemplateLoading(new File(classpath + "/templates/"));
        // 获取指定模板文件,定义模板对象
        Template template = configuration.getTemplate("test1.ftl");
        // 获取数据模型
        Map map = getDatasByMap();
        // 静态化
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        // System.out.println(content);
        InputStream inputStream = IOUtils.toInputStream(content);
        FileOutputStream outputStream = new FileOutputStream(new File("E:\\CodeRepositories\\Online-education-platform\\EduService-parent\\test-freemarker\\StaticFilesDirectory\\test1.html"));
        // 输出文件
        IOUtils.copy(inputStream, outputStream);
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
