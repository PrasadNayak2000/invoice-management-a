package com.eg.invoicemanagement.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResponse {

    private Integer id;

    private Double amount;

    @JsonProperty("paid_amount") //Mapping property paidAmount to paid_amount in response
    private Double paidAmount;

    @JsonProperty("due_date") //Mapping property dueDate to due_date in response
    private LocalDate dueDate;

    private String status;

    //Used in invoice creation response generation
    public InvoiceResponse(Integer id) {
        this.id = id;
    }
}
