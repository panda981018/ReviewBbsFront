package com.example.springsecuritytest.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class BatchResult {

    @Id
    @SequenceGenerator(
            name = "BATCH_RESULT_SEQ",
            sequenceName = "BATCH_RESULT_SEQ",
            allocationSize = 1,
            initialValue = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "BATCH_RESULT_SEQ"
    )
    private Long id;
    private String name;
    private int bbsCount;

    @Builder
    public BatchResult(Long id, String name, int bbsCount) {
        this.id = id;
        this.name = name;
        this.bbsCount = bbsCount;
    }


}
