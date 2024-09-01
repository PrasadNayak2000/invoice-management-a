package com.eg.invoicemanagement.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InvoiceCreationRequest {

    @NotNull(message = "The invoice amount cannot be null") //Verifying amount is not null
    @Min(value = 1, message = "The invoice amount must be more than 0") //Verifying amount is not zero
    private Double amount;

    @NotNull(message = "The invoice due date cannot be null") //Verifying due date is not null
    @JsonProperty("due_date") //Getting value as due_date from request and mapping to dueDate property
    private LocalDate dueDate;
}
