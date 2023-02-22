package org.whitebox.howlook.global.util;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.File;

import java.util.UUID;
@Component
@RequiredArgsConstructor
@Log4j2
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket; // S3 버킷 이름

    // S3로 파일 업로드하기
    public String upload(String inputImagePath)throws RuntimeException {
        String outputImagePath = inputImagePath; // 출력 이미지 파일 경로
        int maxFileSizeInBytes = 3000; // 최대 파일 크기 (단위: 바이트)

        File targetFile = new File(inputImagePath);
        String uploadImageUrl = putS3(targetFile, targetFile.getName()); // s3로업로드
        removeOriginalFile(targetFile);
        return targetFile.getName();
    }

    // S3로 업로드
    private String putS3(File uploadFile, String fileName)throws RuntimeException
    {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName,
                uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }
    //S3 업로드 후 원본 파일 삭제
    private void removeOriginalFile(File targetFile) {
        if (targetFile.exists() && targetFile.delete()) {
            log.info("File delete success");
            return;
        }
        log.info("fail to remove");
    }
    public void removeS3File(String fileName){
        final DeleteObjectRequest deleteObjectRequest = new
                DeleteObjectRequest(bucket, fileName);
        amazonS3Client.deleteObject(deleteObjectRequest);
    }
}