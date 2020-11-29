package org.mineok.manage_cms_client.service;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import org.apache.commons.io.IOUtils;
import org.mineok.manage_cms_client.dao.CMSPageRepository;
import org.mineok.manage_cms_client.dao.CMSSiteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * @Author Gaoming
 * @Email mineok@foxmail.com
 * @Date 2020/11/28/ 20:19
 * @Description
 */
@Service
public class PageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PageService.class);

    @Autowired
    CMSPageRepository cmsPageRepository;

    @Autowired
    CMSSiteRepository cmsSiteRepository;

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    GridFSBucket gridFSBucket;

    // 保存html页面到服务器的物理路径
    public void savePageToServerPath(String pageId) throws IOException {
        CmsPage cmsPage = this.findPageByPageId(pageId);
        // 得到HTML文件的id
        String htmlFileId = cmsPage.getHtmlFileId();
        // 从gridFS中查询HTML文件
        InputStream inputStream = this.getFileByFileId(htmlFileId);
        if (inputStream == null) {
            LOGGER.error("getFileByFileId return InputStream is null , htmlFileId:{}", htmlFileId);
            return;
        }
        // 得到站点的信息
        CmsSite site = this.findSiteBySiteId(cmsPage.getSiteId());
        // 得到站点的物理路径
        String sitePhysicalPath = site.getSitePhysicalPath();
        // 得到站点-页面的物理路径
        String pagePath = sitePhysicalPath + cmsPage.getPagePhysicalPath() + cmsPage.getPageName();
        // 将HTML文件保存在服务器的物理路径上
        FileOutputStream outputStream = new FileOutputStream(new File(pagePath));
        IOUtils.copy(inputStream, outputStream);
        inputStream.close();
        outputStream.close();
    }

    // 根据文件ID从gridFS中查询文件内容
    public InputStream getFileByFileId(String fileId) {
        // 获取文件对象
        GridFSFile fsFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
        // 打开下载流
        GridFSDownloadStream downloadStream = gridFSBucket.openDownloadStream(fsFile.getObjectId());
        // 定义GridFSResource
        GridFsResource resource = new GridFsResource(fsFile, downloadStream);
        try {
            return resource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 根据页面Id查询页面信息
    public CmsPage findPageByPageId(String pageId) {
        Optional<CmsPage> byId = cmsPageRepository.findById(pageId);
        return byId.orElse(null);
    }

    // 根据站点Id查询站点信息
    public CmsSite findSiteBySiteId(String siteId) {
        Optional<CmsSite> byId = cmsSiteRepository.findById(siteId);
        return byId.orElse(null);
    }
}
