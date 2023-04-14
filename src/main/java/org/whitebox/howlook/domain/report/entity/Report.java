package org.whitebox.howlook.domain.report.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    //신고를 하면 management측으로 postId를 넘겨준다.
    @Column
    private Long postId;

    @Column
    private String memberId;
}
