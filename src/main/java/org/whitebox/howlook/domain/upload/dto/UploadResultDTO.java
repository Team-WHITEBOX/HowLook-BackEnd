package org.whitebox.howlook.domain.upload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.whitebox.howlook.domain.feed.entity.Feed;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadResultDTO {
    private Feed feed;
    private String Path; // 사진이 저장될 경로
}
