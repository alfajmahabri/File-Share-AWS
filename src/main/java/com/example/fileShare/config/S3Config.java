package com.example.fileShare.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {


    @Value("${aws.region}")
    private String regionName;

    /* for local machine
    @Value("${aws.accessKeyId}")
    private String access;

    @Value("${aws.secretAccessKey}")
    private String secret;

    /*
        For EC2 auto generated credentials
     */
    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .credentialsProvider(DefaultCredentialsProvider.create())
                .region(Region.of(regionName))
                .build();

    }

    @Bean
    public S3Client client() {

        return S3Client.builder()
                .credentialsProvider(DefaultCredentialsProvider.create())
                .region(Region.of(regionName))
                .build();
    }



    /* for testing from local machine
    @Bean
    public S3Client client(){
        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(access,secret);

        return S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
                .region(Region.of(regionName))
                .build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(access,secret);
        return S3Presigner.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
                .region(Region.of(regionName))
                .build();

    }

     */

}
