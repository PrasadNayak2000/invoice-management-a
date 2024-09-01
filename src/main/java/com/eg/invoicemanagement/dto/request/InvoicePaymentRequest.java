package com.eg.invoicemanagement.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoicePaymentRequest {

    @NotNull(message = "The invoice payment amount cannot be null") //Verifying payment amount is not null
    @Min(value = 1, message = "The invoice payment amount must be more than 0") //Verifying payment amount is not zero
    private Double amount;
}
