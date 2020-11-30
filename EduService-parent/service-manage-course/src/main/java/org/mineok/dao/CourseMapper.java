package org.mineok.dao;

import com.xuecheng.framework.domain.course.CourseBase;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author Gaoming
 * @Email mineok@foxmail.com
 * @Date 2020/11/29/ 18:13
 * @Description
 */
@Mapper
public interface CourseMapper {
    CourseBase findCourseBaseById(String id);
}
