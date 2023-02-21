package org.whitebox.howlook.global.util;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        try{
            multipartFile.transferTo(savePath);
            savePathList.add(savePath.toFile().getAbsolutePath());
        }catch (Exception e){
            log.error("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        return savePathList;
    }
}