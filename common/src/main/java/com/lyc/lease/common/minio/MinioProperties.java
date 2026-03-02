package com.lyc.lease.common.minio;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName MinioProperties
 * @Description TODO Minio属性类
 * @Author fsh
 * @Date 2025/2/2 16:12
 * @Version 1.0
 */

//类属性应该映射到哪个字符串开头的属性
//minio.*

@ConfigurationProperties(prefix = "minio")
@Data
@Component
public class MinioProperties {
    //属性endpoint 自动映射到 minio.endpoint
    private String endpoint;
    //属性accessKey 自动映射到 minio.access-key
    private String accessKey;

    private String secretKey;

    private String bucketName;
}
