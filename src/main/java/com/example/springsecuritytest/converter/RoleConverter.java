package com.example.springsecuritytest.converter;

import com.example.springsecuritytest.enumclass.Role;

import javax.persistence.AttributeConverter;

public class RoleConverter implements AttributeConverter<Role, String> {

    @Override
    // Entity to Database
    public String convertToDatabaseColumn(Role attribute) { // Entity -> DB

        if (attribute == Role.ADMIN) {
            return Role.ADMIN.getValue();
        } else if (attribute == Role.MEMBER) {
            return Role.MEMBER.getValue();
        } else {
            return null;
        }
    }

    @Override
    // Database to Entity
    public Role convertToEntityAttribute(String dbData) { // DB -> Entity

        if (dbData.equals(Role.ADMIN.getValue())) {
            return Role.ADMIN;
        } else if (dbData.equals(Role.MEMBER.getValue())) {
            return Role.MEMBER;
        } else {
            return null;
        }
    }
}
