package org.whitebox.howlook.domain.feed.dto;

import lombok.Data;

@Data
public class HashtagDTO {
    private Long HashtagId;
    private Boolean minimal;
    private Boolean casual;
    private Boolean street;
    private Boolean amekaji;
    private Boolean sporty;
    private Boolean guitar;
}