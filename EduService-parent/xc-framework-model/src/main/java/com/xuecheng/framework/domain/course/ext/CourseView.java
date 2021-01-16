package com.xuecheng.framework.domain.course.ext;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author Gaoming
 * @Email mineok@foxmail.com
 * @Date 2021/01/16/ 15:27
 * @Description
 */
@Data
@NoArgsConstructor
@ToString
public class CourseView implements Serializable {
    // 基础信息
    private CourseBase courseBase;
    // 课程营销
    private CoursePic coursePic;
    // 课程图片
    private CourseMarket courseMarket;
    // 教学计划
    private TeachplanNode teachplanNode;
}
