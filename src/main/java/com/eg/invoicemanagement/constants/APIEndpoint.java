package com.eg.invoicemanagement.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class APIEndpoint {

    public static final String INVOICES = "/invoices";

    public static final String INVOICE_PAYMENT = "/{invoice_id}/payments";

    public static final String OVERDUE_PROCESS = "/process-overdue";
}
