package org.mineok.manage_cms.service;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import org.mineok.manage_cms.config.RabbitMQConfig;
import org.mineok.manage_cms.dao.CMSConfigRepository;
import org.mineok.manage_cms.dao.CMSPageRepository;
import org.mineok.manage_cms.dao.CMSTemplateRepository;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @Author Gaoming
 * @Email mineok@foxmail.com
 * @Date 2020/11/16/ 22:53
 * @Description
 */
@Service
public class PageService {

    @Autowired
    private CMSPageRepository cmsPageRepository;

    @Autowired
    private CMSConfigRepository cmsConfigRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CMSTemplateRepository cmsTemplateRepository;

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    GridFSBucket gridFSBucket;

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 页面查询方法
     *
     * @param page             页码，从1开始记数
     * @param size             每页记录数
     * @param queryPageRequest 查询条件
     * @return
     */
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        if (queryPageRequest == null) {
            queryPageRequest = new QueryPageRequest();
        }
        // 条件值对象
        CmsPage cmsPage = new CmsPage();
        // 设置站点ID
        if (!StringUtils.isEmpty(queryPageRequest.getSiteId())) {
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        // 模板ID
        if (!StringUtils.isEmpty(queryPageRequest.getTemplateId())) {
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        // 站点别名
        if (!StringUtils.isEmpty(queryPageRequest.getPageAliase())) {
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        // 自定义条件查询
        // 定义条件匹配器(根据参数对其模糊查询)
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().
                // ExampleMatcher.GenericPropertyMatchers.contains() 包含关键字
                        withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        // 定义条件对象
        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);
        // 定义分页
        Pageable pageable = PageRequest.of(page - 1, size);
        // 实现自定义条件查询并且分页
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);
        // 定义结果集
        QueryResult queryResult = new QueryResult();
        queryResult.setList(all.getContent());//数据列表
        queryResult.setTotal(all.getTotalElements());//数据总记录数
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return queryResponseResult;
    }

    // 新增页面
//    public CmsPageResult addPage(CmsPage cmsPage) {
//        // 校验页面名称/站点ID/页面webPath的唯一性
//        // 根据页面名称/站点ID/页面webPath去cms_page集合,如果查到说明此页面已经存在,不存在则新增
//        if (cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath()) == null) {
//            // 数据库会自增ID
//            cmsPage.setPageId(null);
//            // 调用dao新增页面
//            cmsPageRepository.save(cmsPage);
//            return new CmsPageResult(CommonCode.SUCCESS, cmsPage);
//        }
//        // 添加失败
//        return new CmsPageResult(CommonCode.FAIL, null);
//    }
    public CmsPageResult addPage(CmsPage cmsPage) {
        if (cmsPage == null) {
            // 参数为空,抛出异常
        }
        // 校验页面名称/站点ID/页面webPath的唯一性
        // 根据页面名称/站点ID/页面webPath去cms_page集合,如果查到说明此页面已经存在,不存在则新增
        if (cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath()) != null) {
            // 页面已经存在
            // 抛出异常:页面已经存在
//            throw new CustomException(CommonCode.FAIL);
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }
        // 数据库会自增ID
        cmsPage.setPageId(null);
        // 调用dao新增页面
        cmsPageRepository.save(cmsPage);
        return new CmsPageResult(CommonCode.SUCCESS, cmsPage);
    }

    // 根据pageId查询指定页面
    public CmsPage findByPageId(String pageId) {
        Optional<CmsPage> byId = cmsPageRepository.findById(pageId);
        return byId.orElse(null);
    }

    // 修改指定页面
    public CmsPageResult updatePage(String pageId, CmsPage newPage) {
        // 根据pageId查询指定页面
        CmsPage oldPage = findByPageId(pageId);
        if (oldPage != null) {
            // 存在则修改oldPage
            //更新模板id
            oldPage.setTemplateId(newPage.getTemplateId());
            //更新所属站点
            oldPage.setSiteId(newPage.getSiteId());
            //更新页面别名
            oldPage.setPageAliase(newPage.getPageAliase());
            //更新页面名称
            oldPage.setPageName(newPage.getPageName());
            //更新访问路径
            oldPage.setPageWebPath(newPage.getPageWebPath());
            //更新物理路径
            oldPage.setPagePhysicalPath(newPage.getPagePhysicalPath());
            // 更新dataURl
            oldPage.setDataUrl(newPage.getDataUrl());
            // 提交修改
            cmsPageRepository.save(oldPage);
            return new CmsPageResult(CommonCode.SUCCESS, oldPage);
        }
        return new CmsPageResult(CommonCode.FAIL, null);
    }

    // 根据pageId删除页面
    public ResponseResult deletePageByPageId(String pageId) {
        if (cmsPageRepository.findById(pageId).isPresent()) {
            cmsPageRepository.deleteById(pageId);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    // 根据Id查询cmsConfig
    public CmsConfig getConfigById(String Id) {
        Optional<CmsConfig> byId = cmsConfigRepository.findById(Id);
        return byId.orElse(null);
    }

    // 总:页面静态化方法
    public String getPageHTML(String pageId) throws IOException, TemplateException {
        // 获取数据模型
        Map model = getModelByPageId(pageId);
        if (model == null) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }
        // 获取页面模板内容字符串
        String template = getTemplateByPageId(pageId);
        if (StringUtils.isEmpty(template)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        // 通过freemaker执行页面静态化
        String html = generateHTML(template, model);
        return html;
    }

    // 3.执行页面静态化,返回静态化后页面内容(模板+数据)
    private String generateHTML(String templateStr, Map model) throws IOException, TemplateException {
        // 定义配置类
        Configuration configuration = new Configuration(Configuration.getVersion());
        // 使用模板加载器将模板字符串变成模板,name为模板名称(targetTemplateName)
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template", templateStr);
        // 设置配置类的模板加载器
        configuration.setTemplateLoader(stringTemplateLoader);
        // 获取模板(根据模板加载器设置的name)
        Template template = configuration.getTemplate("template", "utf-8");
        // 静态化
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
//        // System.out.println(content);
//        InputStream inputStream = IOUtils.toInputStream(content);
//        FileOutputStream outputStream = new FileOutputStream(new File(""));
//        // 输出文件
//        IOUtils.copy(inputStream, outputStream);
//        // 关流
//        inputStream.close();
//        outputStream.close();
        if (!StringUtils.isEmpty(content)) {
            return content;
        }
        return null;
    }

    // 2.获取模板页面信息字符串
    private String getTemplateByPageId(String pageId) throws IOException {
        // 取出页面信息
        CmsPage cmsPage = this.findByPageId(pageId);
        if (cmsPage == null) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        String templateId = cmsPage.getTemplateId();
        if (StringUtils.isEmpty(templateId)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        // 查询模板
        Optional<CmsTemplate> byId = cmsTemplateRepository.findById(templateId);
        if (byId.isPresent()) {
            CmsTemplate cmsTemplate = byId.get();
            // 获取模板文件id
            String templateFileId = cmsTemplate.getTemplateFileId();
            // 通过模板文件ID从gridfs中取模板
            // 根据id查询文件
            GridFSFile gridFSFiles = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
            // 打开一个下载流对象
            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFiles.getObjectId());
            // 创建GridFsResource对象,获取流
            GridFsResource gridFsResource = new GridFsResource(gridFSFiles, gridFSDownloadStream);
            // 从流中取数据
            String content = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
            return content;
        }
        return null;
    }

    // 1.获取数据模型
    private Map getModelByPageId(String pageId) {
        // 取出页面信息
        CmsPage cmsPage = this.findByPageId(pageId);
        if (cmsPage == null) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        // 取出页面的dataURL
        String dataUrl = cmsPage.getDataUrl();
        if (StringUtils.isEmpty(dataUrl)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }
        // 通过restTemplate请求dataURL获取数据
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
        Map body = forEntity.getBody();
        return body;
    }

    // 页面发布
    public ResponseResult post(String pageId) throws IOException, TemplateException {
        // 执行页面静态化
        String pageHTML = this.getPageHTML(pageId);
        // 将页面静态化html文件存储到gridFS中
        CmsPage cmsPage = saveHTML(pageId, pageHTML);
        // 向MQ发送消息
        this.sendPostPage(pageId);
        return ResponseResult.SUCCESS();
    }

    // 将页面静态化html文件存储到gridFS中
    private CmsPage saveHTML(String pageId, String htmlContent) throws IOException {
        // 得到页面信息
        CmsPage cmsPage = this.findByPageId(pageId);
        if (cmsPage == null) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        // 将html文本内容转为输入流
        InputStream inputStream = IOUtils.toInputStream(htmlContent, "utf-8");
        // 将文件流保存到GridFS
        ObjectId objectId = gridFsTemplate.store(inputStream, cmsPage.getPageName());
        // 将html文件id更新到cmsPage
        cmsPage.setHtmlFileId(objectId.toHexString());
        cmsPageRepository.save(cmsPage);
        return cmsPage;
    }

    // 向MQ发送消息
    private void sendPostPage(String pageId) {
        // 创建消息对象
        HashMap<String, String> msg = new HashMap<>();
        msg.put("pageId", pageId);
        // 转为JSON串
        String jsonString = JSON.toJSONString(msg);
        // 获取页面对应的站点id
        CmsPage cmsPage = this.findByPageId(pageId);
        if (cmsPage == null) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        // 发送给MQ(routingKey为页面对应的站点id)
        rabbitTemplate.convertAndSend(RabbitMQConfig.EX_ROUTING_CMS_POSTPAGE, cmsPage.getSiteId(), jsonString);
    }

    // 保存页面
    public CmsPageResult save(CmsPage cmsPage) {
        // 判断页面是否存在
        CmsPage page = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());
        if (!ObjectUtils.isEmpty(page)) {
            return this.updatePage(page.getPageId(), page);
        }
        return this.addPage(page);
    }
}
