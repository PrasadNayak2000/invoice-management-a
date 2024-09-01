package com.eg.invoicemanagement.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationException extends RuntimeException {

    public ValidationException(String msg) {
        super(msg);
    }

}