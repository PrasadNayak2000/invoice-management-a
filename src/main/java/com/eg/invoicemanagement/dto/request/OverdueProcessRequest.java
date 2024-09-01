package com.eg.invoicemanagement.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OverdueProcessRequest {

    @NotNull(message = "The late fee amount for overdue process cannot be null") //Verifying late fee is not null
    @JsonProperty("late_fee") //Getting value as late_fee from request and mapping to lateFee property
    private Double lateFee;

    @NotNull(message = "The overdue days cannot be null") //Verifying overdue days is not null
    @Min(value = 1, message = "The overdue days must be more than 0") //Verifying overdue days is greater than zero
    @JsonProperty("overdue_days") //Getting value as overdue_days from request and mapping to overdueDays property
    private Integer overdueDays;
}
