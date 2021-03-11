package com.awss3springboot;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteBucketRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.Iterator;

public class DeleteBucket {

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

        String bucketLocation = s3Client.getBucketLocation(bucketName);
        ObjectListing objectListing = s3Client.listObjects(bucketName);

        for (Iterator<?> iterator = objectListing.getObjectSummaries().iterator(); iterator.hasNext();){
            S3ObjectSummary objectSummary = (S3ObjectSummary) iterator.next();
            s3Client.deleteObject(bucketName, objectSummary.getKey());
        }

        s3Client.deleteBucket(new DeleteBucketRequest(bucketName));
    }

}