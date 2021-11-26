package com.example.springsecuritytest.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Table(name = "JW_BATCH_RESULT")
public class BatchResult {

    @Id
    @SequenceGenerator(
            name = "BATCH_RESULT_SEQ",
            sequenceName = "JW_BATCH_RESULT_SEQ",
            allocationSize = 1,
            initialValue = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "BATCH_RESULT_SEQ"
    )
    private Long id;
    private String name;
    private LocalDate staticsDate;
    private Long bbsCount;

    @Builder
    public BatchResult(Long id, String name, Long bbsCount, LocalDate staticsDate) {
        this.id = id;
        this.name = name;
        this.bbsCount = bbsCount;
        this.staticsDate = staticsDate;
    }


}
