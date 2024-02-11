package org.once_a_day.file_storage.file_storage_app.service.impl;

import lombok.RequiredArgsConstructor;
import org.once_a_day.file_storage.file_storage_app.service.S3BucketService;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

@Service
@RequiredArgsConstructor
public class S3BucketServiceImpl implements S3BucketService {
    private final S3Client s3Client;

    @Override
    public void uploadFile(String bucket, String fileName, byte[] bytes)
            throws AwsServiceException, SdkClientException {

        int i = 0;
        while (fileExists(bucket, fileName)) {
            final String[] split = fileName.split("\\.");
            fileName = "%s_%d.%s".formatted(split[0], i, split[1]);
        }

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .build();

        s3Client.putObject(request, RequestBody.fromBytes(bytes));
    }

    private boolean fileExists(final String bucket, final String fileName) {
        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .build();

            s3Client.headObject(headObjectRequest);
            return true;
        } catch (S3Exception e) {
            if (e.statusCode() == 404) {
                return false;
            } else {
                throw e;
            }
        }
    }

    @Override
    public byte[] downloadFile(String bucket, String fileName) {
        final var request = GetObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .build();

        ResponseBytes<GetObjectResponse> responseResponseBytes = s3Client.getObjectAsBytes(request);

        return responseResponseBytes.asByteArray();
    }
}
