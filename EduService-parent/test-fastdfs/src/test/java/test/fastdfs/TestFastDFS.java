package test.fastdfs;

import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mineok.test.fastdfs.TestFastDFSApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @Author Gaoming
 * @Email mineok@foxmail.com
 * @Date 2020/12/11/ 20:07
 * @Description
 */
@SpringBootTest(classes = TestFastDFSApplication.class)
@RunWith(SpringRunner.class)
public class TestFastDFS {
    // 上传测试

    @Test
    public void test_upload() throws IOException, MyException {
        // 加载配置文件
        ClientGlobal.initByProperties("config\\fastdfs-client.properties");
        // 定义TrackerClient,用于请求TrackerServer
        TrackerClient trackerClient = new TrackerClient();
        // 连接Tracker
        TrackerServer trackerServer = trackerClient.getConnection();
        // 获取Storage
        StorageServer storeStorage = trackerClient.getStoreStorage(trackerServer);
        // 创建StroageClient
        StorageClient1 storageClient1 = new StorageClient1(trackerServer, storeStorage);
        // 向stroage服务器上传文件
        // 本地文件的路径
        String filePath = "D:\\test_fastdfs.docx";
        String fileId = storageClient1.upload_file1(filePath, "docx", null);
        System.out.println(fileId);
    }

    @Test
    public void test_download() throws IOException, MyException {
        // 加载配置文件
        ClientGlobal.initByProperties("config\\fastdfs-client.properties");
        // 定义TrackerClient,用于请求TrackerServer
        TrackerClient trackerClient = new TrackerClient();
        // 连接Tracker
        TrackerServer trackerServer = trackerClient.getConnection();
        // 获取Storage
        StorageServer storeStorage = trackerClient.getStoreStorage(trackerServer);
        // 创建StroageClient
        StorageClient1 storageClient1 = new StorageClient1(trackerServer, storeStorage);
        // 下载文件
        String fileId = "group1/M00/00/00/rBEZ9l_TavWAGuDHAAHJznqkte842.docx";
        byte[] bytes = storageClient1.download_file1(fileId);
        FileOutputStream fileOutputStream = new FileOutputStream(new File("D:\\test_fastdfs2.docx"));
        fileOutputStream.write(bytes);
    }
}
