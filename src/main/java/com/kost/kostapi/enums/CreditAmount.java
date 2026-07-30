package com.kost.kostapi.enums;

import lombok.Getter;

@Getter
public enum CreditAmount {

    OWNER(0),
    REGULAR(20),
    PREMIUM(40),
    ASK_AVAILABILITY(5);

    private final int value;

    CreditAmount(int value) {
        this.value = value;
    }
}