package org.whitebox.howlook.domain.upload.controller;


import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.whitebox.howlook.domain.post.dto.PostRegisterDTO;
import org.whitebox.howlook.domain.upload.dto.PhotoDTO;
import org.whitebox.howlook.domain.upload.dto.UploadFileDTO;
import org.whitebox.howlook.domain.upload.dto.UploadResultDTO;
import org.whitebox.howlook.domain.upload.service.UploadService;
import org.whitebox.howlook.global.result.ResultResponse;
import org.whitebox.howlook.global.util.LocalUploader;
import org.whitebox.howlook.global.util.S3Uploader;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.whitebox.howlook.global.result.ResultCode.FIND_POST_SUCCESS;

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
        // post에 사진을 업로드하기 위한 기반 코드
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
//                Long pId = uploadFileDTO.getpostId();
////                UploadResultDTO temp = UploadResultDTO.builder().Path(uploadPath+"\\"+uuid+"_"+originalName).postId(pId).build();
//                list.add(temp);
//
//                uploadService.register(temp);
//
//            });
  //      }
        return list;
    }

    // 게시글 아이디에 붙어있는 사진 경로를 반환
    @ApiOperation(value = "Get photo by postid", notes = "Get 방식으로 게시글에 붙은 사진 경로 조회")
    @GetMapping("/read")
    public List<PhotoDTO> readPhoto(Long postId) {
        List<PhotoDTO> list = new ArrayList<>();
        list = uploadService.getPhotoData(postId);

        return list;
    }

    // 게시글 아이디에 붙어있는 사진 경로를 반환
    @ApiOperation(value = "Get photo by photoid", notes = "Get 방식으로 포토아이디로 사진 경로 조회")
    @GetMapping("/readbypid")
    public String readPhotoByPhotoId(Long PhotoId) {
        return uploadService.getPathByPhotoId(PhotoId);
    }

    // 파일경로를 통해 사진 가져오는 Get Method
    // postID로 가져오는 거 구현해야함
    @ApiOperation(value = "view 파일", notes = "GET방식으로 첨부파일 조회")
    @GetMapping("/view/{fileName}")
    public ResponseEntity<ResultResponse> viewFileGET(@PathVariable String fileName){

        String urls = "https://howlook-s3-bucket.s3.ap-northeast-2.amazonaws.com/" + fileName;

        return ResponseEntity.ok(ResultResponse.of(FIND_POST_SUCCESS,urls));
    }

    private final LocalUploader localUploader;
    private final S3Uploader s3Uploader;
    @PostMapping(value = "/S3Upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<String> S3Upload(@Valid @ModelAttribute PostRegisterDTO postRegisterDTO){
        List<MultipartFile> files = postRegisterDTO.getUploadFileDTO().getFiles();
        if(files == null || files.size() == 0){
            return null;
        }
        List<String> uploadedFilePaths = new ArrayList<>();
        for(MultipartFile file:files){
            uploadedFilePaths.addAll(localUploader.uploadLocal(file));
        }
        log.info("----------------------------");
        log.info(uploadedFilePaths);
        List<String> s3Paths =
                uploadedFilePaths.stream().map(s3Uploader::upload).collect(Collectors.toList());
        return s3Paths;
    }
}
