package com.example.springsecuritytest.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Gender {

    MALE("Male"),
    FEMALE("Female");

    private String value;
}
