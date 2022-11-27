package org.whitebox.howlook.domain.feed.dto;

import lombok.Data;
import org.whitebox.howlook.domain.feed.entity.Hashtag;

@Data
public class HashtagDTO {
    //private Long HashtagId;
    private Boolean minimal;
    private Boolean casual;
    private Boolean street;
    private Boolean amekaji;
    private Boolean sporty;
    private Boolean guitar;

    public HashtagDTO(Hashtag hashtag) {
        this.minimal = hashtag.getMinimal();
        this.casual = hashtag.getCasual();
        this.amekaji = hashtag.getAmekaji();
        this.street = hashtag.getStreet();
        this.sporty = hashtag.getSporty();
        this.guitar = hashtag.getGuitar();
    }
}