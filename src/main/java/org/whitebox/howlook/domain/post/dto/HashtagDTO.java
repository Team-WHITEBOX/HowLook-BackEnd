package org.whitebox.howlook.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.whitebox.howlook.domain.post.entity.Hashtag;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HashtagDTO {

    @NotNull
    private Boolean minimal;

    @NotNull
    private Boolean casual;

    @NotNull
    private Boolean street;

    @NotNull
    private Boolean amekaji;

    @NotNull
    private Boolean sporty;

    @NotNull
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