package com.example.springsecuritytest.enumclass;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Gender {

    MALE("Male"),
    FEMALE("Female");

    private String value;
}
