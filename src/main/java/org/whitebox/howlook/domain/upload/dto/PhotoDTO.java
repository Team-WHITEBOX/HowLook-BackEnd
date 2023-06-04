package org.whitebox.howlook.domain.upload.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PhotoDTO {
    private String path;
    private Long photoId;
}
