package com.jinyu.blog.config;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author <a href="jinyu52370@163.com">JJJ</a>
 * @date 2020/6/22 14:28
 */
public class UploadFileUtils {
    /**
     * 将base64编码转换为的字节数组decode，再转换为MultipartFile对象
     * @param base64 base64编码
     * @return MultipartFile
     * @throws IOException
     */
    public static MultipartFile base64DecodeTransformToMultipartFile(String base64) throws IOException {
        return new MockMultipartFile(MediaType.APPLICATION_OCTET_STREAM_VALUE, new ByteArrayInputStream(base64Decode(base64)));
    }

    /**
     * 将base64编码转换为的字节数组decode
     * @param base64 base64编码
     * @return 字节数组decode
     */
    public static byte[] base64Decode(String base64) {
        return Base64.getDecoder().decode(base64.split(",")[1].getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 创建storage的客户端
     * @return storage的客户端
     */
    public static StorageClient1 getStorageClient1() throws Exception {
        //加载fastDFS客户端的配置文件
        ClientGlobal.initByProperties("fastdfs-client.properties");
        //创建tracker的客户端
        TrackerServer trackerServer = new TrackerClient().getConnection();
        //创建storage的客户端
        return new StorageClient1(trackerServer, null);
    }
}
