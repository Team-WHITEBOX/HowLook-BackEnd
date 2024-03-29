package org.whitebox.howlook.domain.upload.service;

import org.whitebox.howlook.domain.upload.dto.PhotoDTO;
import org.whitebox.howlook.domain.upload.dto.UploadResultDTO;

import java.util.List;

public interface UploadService{
        // 게시글 register와 동일한 사진 register
        public void register(UploadResultDTO uploadResultDTO);

        // 경로를 가져와 문자열 리스트를 반환
        public List<String> getPath(Long postId);

        public List<PhotoDTO> getPhotoData(Long postId);

        // 사진 아이디로 경로를 반환
        public String getPathByPhotoId(Long PhotoId);
}
