package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Author Gaoming
 * @Email mineok@foxmail.com
 * @Date 2020/11/30/ 16:53
 * @Description 课程管理Api接口
 */
@Api(value = "课程管理接口", description = "课程管理接口,提供课程增删改查")
public interface CourseControllerApi {

    @ApiOperation("课程计划查询")
    Teachplan findTeachplanList(String courseId);

    @ApiOperation("添加课程计划")
    ResponseResult addTeachplan(Teachplan teachplan);

    @ApiOperation("添加课程与图片的关联信息")
    public ResponseResult addCoursePic(String courseId, String pic);

    @ApiOperation("查询课程图片")
    public CoursePic findCoursePic(String courseId);

    @ApiOperation("删除课程图片")
    public ResponseResult deleteCoursePic(String courseId);


}
