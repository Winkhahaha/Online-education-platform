package org.mineok.dao;

import com.xuecheng.framework.domain.course.Teachplan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author Gaoming
 * @Email mineok@foxmail.com
 * @Date 2020/11/29/ 18:13
 * @Description
 */
public interface TeachplanRepository extends JpaRepository<Teachplan, String> {

    // 根据课程Id和parentId查询Teachplan
    List<Teachplan> findByCourseidAndParentid(String courseId,String parentId);
}
