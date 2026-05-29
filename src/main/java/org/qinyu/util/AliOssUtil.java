package org.qinyu.util;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;


@Data
@Slf4j
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
public class AliOssUtil {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

    /**
     * 文件上传
     *
     * @param bytes      文件字节数组
     * @param objectName 文件在OSS中的存储名称
     * @return 文件访问URL
     */
    public String upload(byte[] bytes, String objectName) {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(bytes));
            String url = String.format("https://%s.%s/%s", bucketName, endpoint, objectName);
            log.info("文件上传成功: {}", url);
            return url;
        } catch (OSSException | ClientException e) {
            log.error("OSS上传失败, objectName: {}", objectName, e);
            throw new RuntimeException("OSS上传失败: " + e.getMessage(), e);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}
