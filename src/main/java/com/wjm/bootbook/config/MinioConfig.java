package com.wjm.bootbook.config;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author stephen wang
 */
@Configuration
@Component
@Slf4j
public class MinioConfig implements InitializingBean {
    @Value("${minio.host}")
    private String host;
    @Value("${minio.url}")
    private String url;
    @Value("${minio.accessKey}")
    private String accessKey;
    @Value("${minio.secretKey}")
    private String secretKey;
    @Value("${minio.bucket}")
    private String bucket;

    private MinioClient minioClient;

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.hasText(url, "Minio url 为空");
        Assert.hasText(accessKey, "Minio accessKey为空");
        Assert.hasText(secretKey, "Minio secretKey为空");
        this.minioClient = MinioClient.builder().endpoint(host)
                .credentials(accessKey, secretKey)
                .build();
    }

    public String putObject(MultipartFile file, String objName) throws Exception {
        String[] split = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
        String type = split[split.length - 1];
        String remoteFileName = objName + "." + type;
        try (InputStream stream = file.getInputStream()) {
            PutObjectArgs putObjectArgs = new PutObjectArgs.Builder()
                    .object(remoteFileName)
                    .stream(stream, file.getSize(), PutObjectArgs.MIN_MULTIPART_SIZE)
                    .contentType(file.getContentType())
                    .bucket(this.bucket).build();
            minioClient.putObject(putObjectArgs);
            return this.url + UriUtils.encode(remoteFileName, StandardCharsets.UTF_8);
        }
    }

    public void removeObject(String objName) throws Exception {
        RemoveObjectArgs args = RemoveObjectArgs.builder()
                .object(objName)
                .bucket(this.bucket).build();
        minioClient.removeObject(args);
        log.info("MinIO File: " + objName + " has been deleted!");
    }

}
