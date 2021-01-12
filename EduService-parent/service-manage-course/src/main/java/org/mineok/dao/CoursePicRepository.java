package org.mineok.dao;

import com.xuecheng.framework.domain.course.CoursePic;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author Gaoming
 * @Email mineok@foxmail.com
 * @Date 2021/01/12/ 16:22
 * @Description
 */
public interface CoursePicRepository extends JpaRepository<CoursePic, String> {

    //删除成功返回1否则返回0
    long deleteByCourseid(String courseId);
}
