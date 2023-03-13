package org.whitebox.howlook.global.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Log4j2
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket; // S3 버킷 이름

    // S3로 파일 업로드하기
    public String upload(String inputImagePath)throws RuntimeException {
        String outputImagePath = getResizedFileName(inputImagePath); // 출력 이미지 파일 경로
        int maxFileSizeInBytes = 3000; // 최대 파일 크기 (단위: 바이트)

        log.info(outputImagePath);

        File targetFile = new File(inputImagePath);

        try{
            targetFile = reSize(new File(inputImagePath),3000);
        }catch (Exception e){
            log.error("ERROR: " + e.getMessage());
        }

        String uploadImageUrl = putS3(targetFile, outputImagePath); // s3로업로드
        removeOriginalFile(targetFile);

        return outputImagePath;
    }

    public File reSize(File file, int maxSize) throws IOException {
        // 이미지를 지정된 maxSize 이하의 크기로 조정
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(file).size(maxSize, maxSize).toOutputStream(outputStream);

        byte[] bytes = outputStream.toByteArray();
        File compressedImg = new File(file.getName());
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bytes);
        fos.close();

        return compressedImg;
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

    public static String getResizedFileName(String fileName) {
        int index = fileName.lastIndexOf("."); // 파일 이름에서 마지막 . 위치 찾기
        String name = fileName.substring(0, index); // 파일 이름에서 확장자를 제외한 부분 추출
        String ext = fileName.substring(index); // 파일 이름에서 확장자 추출
        return name + "-resizing" + ext; // 새 파일 이름 생성
    }
}