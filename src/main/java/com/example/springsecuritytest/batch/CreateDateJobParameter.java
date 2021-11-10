package com.example.springsecuritytest.batch;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@NoArgsConstructor
@Getter
public class CreateDateJobParameter {
    private LocalDate createDate;

    @Value("#{jobParameters[createdDate]}")
    public void setCreateDate(String createDate) {
        this.createDate = LocalDate.parse(createDate, DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
