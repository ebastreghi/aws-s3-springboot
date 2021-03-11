package com.awss3springboot;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectTagging;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.Tag;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CreatePutObjectTag {

    @Value("${aws.access-key}")
    private static String accessKey;

    @Value("${aws.secret-key}")
    private static String secretKey;

    @Value("${aws.sqs-resource}")
    private static String sqsResource;

    public static void main(String args[]) throws IOException {
        String bucketName = "aws-s3-springboot";
        String fileName = "sampleImg.png";

        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        AmazonS3 s3Client = AmazonS3Client.builder().withRegion(Regions.US_EAST_1)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).build();

        File file = new File(ClassLoader.getSystemResource(fileName).getFile());

        List<Tag> tags = new ArrayList<Tag>();
        tags.add(new Tag("image", "true"));
        tags.add(new Tag("PII", "false"));

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, file);
        putObjectRequest.setTagging(new ObjectTagging(tags));
        s3Client.putObject(putObjectRequest);
        System.out.println("Uploading object into S3");
    }

}