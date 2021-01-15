package manage_course.dao;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mineok.ManageCourseApplication;
import org.mineok.dao.CourseBaseRepository;
import org.mineok.dao.CourseMapper;
import org.mineok.dao.TeachplanMapper;
import org.mineok.feign_client.CmsPageFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

/**
 * @author Administrator
 * @version 1.0
 **/
@SpringBootTest(classes = ManageCourseApplication.class)
@RunWith(SpringRunner.class)
public class TestRibbon {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    CmsPageFeignClient feignClient;

    @Test
    public void testFeign() {
        // 确定要获取的服务名称
        for (int i = 0; i < 3; i++) {
            String payment = feignClient.getPayment(2);
            System.out.println(payment);
        }
    }

    @Test
    public void testRibbon() {
        // 确定要获取的服务名称
        String serviceId = "xc-service-manage-cms";
        for (int i = 0; i < 5; i++) {
            ResponseEntity<String> entity = restTemplate.getForEntity("http://" + serviceId + "/cms/page/nacos/5", String.class);
            String body = entity.getBody();
            System.out.println(body);
        }
    }
}
