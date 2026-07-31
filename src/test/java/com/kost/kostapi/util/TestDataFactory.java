package com.kost.kostapi.util;

import com.kost.kostapi.entity.User;
import com.kost.kostapi.enums.UserRole;

public final class TestDataFactory {

    private TestDataFactory() {
    }

    public static User owner() {

        return User.builder()
                .name("Owner")
                .email("owner@test.com")
                .password("password")
                .role(UserRole.OWNER)
                .credit(0)
                .build();
    }

    public static User regular() {

        return User.builder()
                .name("Regular")
                .email("regular@test.com")
                .password("password")
                .role(UserRole.REGULAR)
                .credit(20)
                .build();
    }
}