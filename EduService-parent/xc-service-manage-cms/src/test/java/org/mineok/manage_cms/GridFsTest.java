package org.mineok.manage_cms;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-09-12 18:11
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class GridFsTest {

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    GridFSBucket gridFSBucket;

    @Test   // 存文件
    public void testStore() throws FileNotFoundException {
        // 定义File
        File file = new File("E:\\CodeRepositories\\Online-education-platform\\EduService-parent\\test-freemarker\\src\\main\\resources\\templates\\index_banner.ftl");
        FileInputStream fileInputStream = new FileInputStream(file);
        // 获取文件Id
        ObjectId objectId = gridFsTemplate.store(fileInputStream, "index_banner.ftl");
        System.out.println(objectId);
    }

    @Test   // 取文件
    public void queryFile() throws IOException {
        // 根据id查询文件
        GridFSFile gridFSFiles = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is("5fc353394c52822a60a993f4")));
        // 打开一个下载流对象
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFiles.getObjectId());
        // 创建GridFsResource对象,获取流
        GridFsResource gridFsResource = new GridFsResource(gridFSFiles, gridFSDownloadStream);
        // 从流中取数据
        String content = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
        System.out.println(content);
    }
}
