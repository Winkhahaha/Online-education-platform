package org.mineok.dao;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author Gaoming
 * @Email mineok@foxmail.com
 * @Date 2020/11/29/ 18:13
 * @Description
 */
public interface CourseMarketRepository extends JpaRepository<CourseMarket, String> {
}
