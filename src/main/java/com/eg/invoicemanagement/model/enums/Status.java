package com.eg.invoicemanagement.model.enums;

import lombok.Getter;

@Getter
public enum Status {

    PENDING("pending"),
    PAID("paid"),
    VOID("void");

    private final String value;

    Status(String value) {
        this.value = value;
    }
}
