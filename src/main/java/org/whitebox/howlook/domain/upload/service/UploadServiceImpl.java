package org.whitebox.howlook.domain.upload.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.whitebox.howlook.domain.upload.dto.PhotoDTO;
import org.whitebox.howlook.domain.upload.dto.UploadResultDTO;
import org.whitebox.howlook.domain.upload.entity.Upload;
import org.whitebox.howlook.domain.upload.repository.UploadRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
@Data
public class UploadServiceImpl implements UploadService{

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final ModelMapper modelMapper;
    private final UploadRepository uploadRepository;

    @Value("${org.whitebox.dbname}")
    private String database; // 저장될 경로

    @Override // 게시글 ID와 사진 경로를 Insert하는 함수
    public void register(UploadResultDTO uploadResultDTO) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Upload upload = modelMapper.map(uploadResultDTO, Upload.class);
        uploadRepository.save(upload);
    }

    public List<PhotoDTO> getPhtoData(Long NPostId)
    {
        List<Upload> uploads = uploadRepository.findByPostId(NPostId);
        List<PhotoDTO> photoDTOS = new ArrayList<>();

        for(Upload u : uploads)
        {
            PhotoDTO temp = new PhotoDTO();
            temp.setPath(u.getPath());
            temp.setPhotoId(u.getPhotoId());

            photoDTOS.add(temp);
        }

        return photoDTOS;
    }
    @Override // DB로 부터 입력받은 ID에 붙은 사진 경로를 가져오는 함수
    public List<String> getPath(Long NPostId) {
        List<Map<String, Object>> cnt = new ArrayList<>();
        List<String> str = new ArrayList<>();
        String sql = "SELECT path FROM "+database+".upload WHERE npost_id = ?";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, NPostId);
        cnt.addAll(rows);

        for(Map<String, Object> map: cnt) {
            for (String path: map.keySet()){
                str.add((String) map.get(path));
            }
        }
        return str;
    }

    @Override
    public String getPathByPhotoId(Long PhotoId)
    {
        return uploadRepository.findByPhotoId(PhotoId).getPath();
    }
}
