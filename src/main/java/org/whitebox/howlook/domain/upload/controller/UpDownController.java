package org.whitebox.howlook.domain.upload.controller;


import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.whitebox.howlook.domain.upload.dto.UploadFileDTO;
import org.whitebox.howlook.domain.upload.dto.UploadResultDTO;
import org.whitebox.howlook.domain.upload.service.UploadService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/photo")
@Log4j2
@RequiredArgsConstructor
public class UpDownController {
    private final UploadService uploadService; // 업로드 서비스
    @Value("${org.whitebox.upload.path}")
    private String uploadPath; // 저장될 경로

    // 게시글 업로드 시 붙은 사진 같이 업로드하기 위해 POST
    @ApiOperation(value = "Upload POST", notes = "POST 방식으로 파일 등록")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<UploadResultDTO> upload(UploadFileDTO uploadFileDTO)
    {
        // 사진 업로드 코드
        log.info(uploadFileDTO);
        final List<UploadResultDTO> list = new ArrayList<>();
        // feed에 사진을 업로드하기 위한 기반 코드
//        if(uploadFileDTO.getFiles() != null)
//        {
//            // forEach 문으로 선택한 사진 수 만큼 실행 됨
//            uploadFileDTO.getFiles().forEach(multipartFile -> {
//                String originalName = multipartFile.getOriginalFilename();
//                String uuid = UUID.randomUUID().toString();
//
//                Path savePath = Paths.get(uploadPath, uuid+"_"+originalName);
//
//                try{
//                    multipartFile.transferTo(savePath);
//                }catch (IOException e)
//                {
//                    e.printStackTrace();
//                }
//
//                // 여기까지는 사진을 Server에 저장하는 코드
//                // 여기서부터 사진 정보를 DB에 저장하는 코드
//                // UploadFileDTO를 통해 db에 사진 저장경로를 Insert
//                Long pId = uploadFileDTO.getNPostId();
////                UploadResultDTO temp = UploadResultDTO.builder().Path(uploadPath+"\\"+uuid+"_"+originalName).NPostId(pId).build();
//                list.add(temp);
//
//                uploadService.register(temp);
//
//            });
  //      }
        return list;
    }


    // 게시글 아이디에 붙어있는 사진 경로를 반환
    @ApiOperation(value = "readPhoto Get", notes = "Get 방식으로 사진 경로 조회")
    @GetMapping("/read")
    public List<String> readPhoto(Long NPostId) {
        List<String> list = new ArrayList<>();
        list = uploadService.getPath(NPostId);

        for(String path: list) {
            System.out.println(path);
        }

        return list;
    }

//    @GetMapping("/read")
//    public ResponseEntity<Resource> readPhoto(Long NPostId) {
//        //List<UploadResultDTO> list = new ArrayList<>();
//        //list.addAll(uploadService.reader(NPostId));
//        //log.info(list);
//        List<String> list = new ArrayList<>();
//        list = uploadService.getPath(NPostId);
//
//        HttpHeaders headers = new HttpHeaders();
//        Resource resource = new FileSystemResource("");
//
//        for(String path: list) {
//            System.out.println(path);
//            resource = new FileSystemResource(path);
//            String resourceName = resource.getFilename();
//
//            try{
//                headers.add("Content-Type", Files.probeContentType( resource.getFile().toPath() ));
//            } catch(Exception e){
//                return ResponseEntity.internalServerError().build();
//            }
//        }
//
//        return ResponseEntity.ok().headers(headers).body(resource);
//    }


    // 파일경로를 통해 사진 가져오는 Get Method
    // postID로 가져오는 거 구현해야함
    @ApiOperation(value = "view 파일", notes = "GET방식으로 첨부파일 조회")
    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable String fileName){

        Resource resource = new FileSystemResource(uploadPath+ File.separator + fileName);
        String resourceName = resource.getFilename();
        HttpHeaders headers = new HttpHeaders();

        try{
            headers.add("Content-Type", Files.probeContentType( resource.getFile().toPath() ));
        } catch(Exception e){
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().headers(headers).body(resource);
    }
}
