package org.layer.ncp.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NcpConfig {
    @Value("${ncp.storage.accessKey}")
    private String accessKey;

    @Value("${ncp.storage.secretKey}")
    private String secretKey;

    @Value("${ncp.storage.region}")
    private String region;

    @Value("${ncp.storage.endpoint}")
    private String endPoint;


    @Bean
    public AmazonS3Client amazonS3Client() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey,
                secretKey);
        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint,
                        region))
                .build();
    }
}
