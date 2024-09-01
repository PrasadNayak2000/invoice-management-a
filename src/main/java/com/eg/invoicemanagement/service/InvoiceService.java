package com.eg.invoicemanagement.service;

import com.eg.invoicemanagement.dto.request.InvoiceCreationRequest;
import com.eg.invoicemanagement.dto.request.InvoicePaymentRequest;
import com.eg.invoicemanagement.dto.request.OverdueProcessRequest;
import org.springframework.http.ResponseEntity;

public interface InvoiceService {

    ResponseEntity<Object> createInvoice(InvoiceCreationRequest request);

    ResponseEntity<Object> getAllInvoices();

    ResponseEntity<Object> doPayment(Integer invoiceId, InvoicePaymentRequest request);

    ResponseEntity<Object> processOverdue(OverdueProcessRequest request);
}
