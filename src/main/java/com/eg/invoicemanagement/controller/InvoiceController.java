package com.eg.invoicemanagement.controller;

import com.eg.invoicemanagement.dto.request.InvoiceCreationRequest;
import com.eg.invoicemanagement.dto.request.InvoicePaymentRequest;
import com.eg.invoicemanagement.dto.request.OverdueProcessRequest;
import com.eg.invoicemanagement.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.eg.invoicemanagement.constants.APIEndpoint.INVOICES;
import static com.eg.invoicemanagement.constants.APIEndpoint.INVOICE_PAYMENT;
import static com.eg.invoicemanagement.constants.APIEndpoint.OVERDUE_PROCESS;

@RestController
@Log4j2
@RequestMapping(INVOICES)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createInvoice(@Validated @RequestBody InvoiceCreationRequest request) {
        log.info("Create Invoice : {}", request.toString());
        return invoiceService.createInvoice(request);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAllInvoices() {
        log.info("Getting all Invoices");
        return invoiceService.getAllInvoices();
    }

    @PutMapping(value = INVOICE_PAYMENT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> doPayment(@PathVariable("invoice_id") Integer invoiceId,
                                            @Validated @RequestBody InvoicePaymentRequest request) {
        log.info("Invoice payment amount : {} for invoice id : {}", request.getAmount(), invoiceId);
        return invoiceService.doPayment(invoiceId, request);
    }

    @PutMapping(value = OVERDUE_PROCESS, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> processOverdue(@Validated @RequestBody OverdueProcessRequest request) {
        log.info("Processing all overdue invoices : {}", request.toString());
        return invoiceService.processOverdue(request);
    }
}
