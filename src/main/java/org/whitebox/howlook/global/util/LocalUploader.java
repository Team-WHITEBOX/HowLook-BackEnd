package org.whitebox.howlook.global.util;

import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

@Component
@Log4j2
public class LocalUploader {
    @Value("${org.whitebox.upload.path}")// import 시에 springframework
    private String uploadPath;

    public List<String> uploadLocal(MultipartFile multipartFile){

        if(multipartFile == null || multipartFile.isEmpty()){
            return null;
        }

        String uuid = UUID.randomUUID().toString();
        String saveFileName = uuid+"_"+ multipartFile.getOriginalFilename();
        Path savePath = Paths.get(uploadPath, saveFileName);
        List<String> savePathList = new ArrayList<>();

        //File file = new File(multipartFile.getOriginalFilename());
        try {
            multipartFile = resizeImage(multipartFile, 3000);
        }catch(Exception e)
        {
            log.error("ERROR: " + e.getMessage());
            e.printStackTrace();
        }

        try{
            multipartFile.transferTo(savePath);
            savePathList.add(savePath.toFile().getAbsolutePath());
        }catch (Exception e){
            log.error("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        return savePathList;
    }

    public MultipartFile resizeImage(MultipartFile multipartFile, int maxSize) throws IOException {
        // 이미지를 지정된 maxSize 이하의 크기로 조정
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(multipartFile.getInputStream()).size(maxSize, maxSize).toOutputStream(outputStream);

        // byte 배열로 변환하여 반환
        return convert(outputStream.toByteArray(),multipartFile.getName(),multipartFile.getContentType());
    }

    public MultipartFile convert(byte[] bytes, String filename, String contentType) throws IOException {
        return new MockMultipartFile(filename, filename, contentType, bytes);
    }
}