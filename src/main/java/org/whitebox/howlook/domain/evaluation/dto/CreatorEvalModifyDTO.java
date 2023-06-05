package org.whitebox.howlook.domain.evaluation.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreatorEvalModifyDTO {
    long CreatorEvalId; // 수정할 평가글 아이디

    String content; // 수정 글 내용
}
