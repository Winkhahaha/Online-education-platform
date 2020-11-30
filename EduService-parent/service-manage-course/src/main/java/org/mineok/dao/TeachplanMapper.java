package org.mineok.dao;

import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author Gaoming
 * @Email mineok@foxmail.com
 * @Date 2020/11/30/ 17:22
 * @Description
 */
@Mapper
public interface TeachplanMapper {
    // 课程计划查询
    TeachplanNode selectList(String courseId);
}
