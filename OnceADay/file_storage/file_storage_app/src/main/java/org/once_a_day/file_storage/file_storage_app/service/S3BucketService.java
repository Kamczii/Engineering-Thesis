package org.once_a_day.file_storage.file_storage_app.service;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;

public interface S3BucketService {

    void uploadFile(String bucket, String fileName, byte[] bytes)
            throws S3Exception, AwsServiceException, SdkClientException, IOException;

    byte[] downloadFile(String bucket, String fileName);
}
