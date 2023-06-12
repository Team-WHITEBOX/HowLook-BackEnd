package org.whitebox.howlook.domain.member.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;


@Entity
public class Creator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long creatorId;

    String userId;
}
