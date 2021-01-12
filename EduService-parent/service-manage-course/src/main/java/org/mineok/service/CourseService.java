package org.mineok.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import org.mineok.dao.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * @Author Gaoming
 * @Email mineok@foxmail.com
 * @Date 2020/11/30/ 17:37
 * @Description
 */
@Service
public class CourseService {
    @Autowired
    TeachplanMapper teachplanMapper;
    @Autowired
    TeachplanRepository teachplanRepository;
    @Autowired
    CourseBaseRepository courseBaseRepository;
    @Autowired
    CourseMapper courseMapper;
    @Autowired
    CoursePicRepository coursePicRepository;

    // 课程计划(树)查询
    public TeachplanNode findTeachplan(String courseId) {
        return teachplanMapper.selectList(courseId);
    }

    // 添加课程计划
    @Transactional
    public ResponseResult addTeachplan(Teachplan teachplan) {
        if (teachplan == null ||
                //StringUtils.isEmpty(teachplan.getId()) ||
                StringUtils.isEmpty(teachplan.getPname())) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        String courseid = teachplan.getCourseid();
        String parentid = teachplan.getParentid();
        if (StringUtils.isEmpty(parentid)) {
            // 取出该课程的根课程id
            parentid = this.getTeachplanRootNode(courseid);
        }
        // 查询父节点
        Teachplan teachplanRoot = teachplanRepository.findById(parentid).get();
        // 添加新节点
        // 将页面提交的teachplan拷贝到new
        Teachplan teachplanNew = new Teachplan();
        BeanUtils.copyProperties(teachplan, teachplanNew);
        teachplanNew.setParentid(parentid);
        teachplanNew.setCourseid(courseid);
        if (teachplanRoot.getGrade().equals("1")) {
            teachplanNew.setGrade("2"); // 级别,根据父节点级别设置
        } else {
            teachplanNew.setGrade("3");
        }
        teachplanRepository.save(teachplanNew);
        return ResponseResult.SUCCESS();
    }


    // 查询课程在课程计划表中的根节点,查询不到则自动添加到课程计划表中作为根节点
    private String getTeachplanRootNode(String courseId) {
        Optional<CourseBase> courseOpt = courseBaseRepository.findById(courseId);
        if (!courseOpt.isPresent()) {
            return null;
        }
        CourseBase course = courseOpt.get();
        // 查询课程的根节点
        List<Teachplan> teachplanList = teachplanRepository.findByCourseidAndParentid(courseId, "0");
        if (teachplanList == null || teachplanList.size() <= 0) {
            // 查询不到,该课程自己成为根节点并添加到课程计划表
            Teachplan teachplan = new Teachplan();
            teachplan.setParentid("0");
            teachplan.setGrade("1");
            teachplan.setPname(course.getName());
            teachplan.setCourseid(course.getId());
            teachplan.setStatus("0");
            teachplanRepository.save(teachplan);
            return teachplan.getId();
        }
        // 发现该课程在课程计划表中存在根节点,则返回根节点Id
        return teachplanList.get(0).getId();
    }

    // 查询所有课程
    public QueryResponseResult getCourseList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<CourseBase> courseList = courseMapper.findCourseList();
        if (courseList.getResult() == null || courseList.getTotal() <= 0) {
            return new QueryResponseResult(CommonCode.FAIL, null);
        }
        // 创建结果集
        return new QueryResponseResult(CommonCode.SUCCESS, new QueryResult<>(courseList.getResult(), courseList.getTotal()));
    }

    // 添加课程与图片的关联信息(关联表)
    @Transactional
    public ResponseResult saveCoursePic(String courseId, String pic) {
        // 查询课程图片
        Optional<CoursePic> picOptional = coursePicRepository.findById(courseId);
        CoursePic coursePic = null;
        if (picOptional.isPresent()) {
            coursePic = picOptional.get();
        }
        // 没有课程图片则新建对象
        if (ObjectUtils.isEmpty(coursePic)) {
            coursePic = new CoursePic();
        }
        coursePic.setPic(pic);
        coursePic.setCourseid(courseId);
        coursePicRepository.save(coursePic);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    // 查询课程图片
    public CoursePic findCoursepic(String courseId) {
        Optional<CoursePic> coursePic = coursePicRepository.findById(courseId);
        return coursePic.orElse(null);
    }

    //删除课程图片
    @Transactional
    public ResponseResult deleteCoursePic(String courseId) {
        //执行删除，返回1表示删除成功，返回0表示删除失败
        long result = coursePicRepository.deleteByCourseid(courseId);
        if (result > 0) {
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

}
