package com.example.bigbrotherbe.global.file.entity;

import com.example.bigbrotherbe.domain.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.checkerframework.checker.units.qual.A;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class File extends BaseTimeEntity {

    @Id
    private Long id;

    private String fileType;

    private String url;
}
