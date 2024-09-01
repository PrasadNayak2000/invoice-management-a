package com.eg.invoicemanagement.service;


import com.eg.invoicemanagement.dto.request.InvoiceCreationRequest;
import com.eg.invoicemanagement.dto.request.InvoicePaymentRequest;
import com.eg.invoicemanagement.dto.request.OverdueProcessRequest;
import com.eg.invoicemanagement.dto.response.InvoiceResponse;
import com.eg.invoicemanagement.service.impl.InvoiceServiceImpl;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class InvoiceServiceTest {

    private final InvoiceServiceImpl invoiceService = new InvoiceServiceImpl();

    @BeforeAll
    public static void beforeAll() {
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateInvoice() {
        InvoiceCreationRequest request = new InvoiceCreationRequest();
        request.setAmount(1000.00);
        request.setDueDate(LocalDate.now().plusDays(1)); // Tomorrow

        ResponseEntity<Object> response = invoiceService.createInvoice(request);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetAllInvoices() {
        testCreateInvoice(); // Create an invoice to test retrieval
        ResponseEntity<Object> response = invoiceService.getAllInvoices();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        List<InvoiceResponse> invoices = (List<InvoiceResponse>) response.getBody();
        assertEquals(1, invoices.size());
    }

    @Test
    void testDoPayment() {
        InvoiceCreationRequest creationRequest = new InvoiceCreationRequest();
        creationRequest.setAmount(500.00);
        creationRequest.setDueDate(LocalDate.now()); //Today
        invoiceService.createInvoice(creationRequest);

        InvoicePaymentRequest paymentRequest = new InvoicePaymentRequest();
        paymentRequest.setAmount(200.00);

        ResponseEntity<Object> response = invoiceService.doPayment(1, paymentRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testDoPaymentFailureDueToOverdue() {
        //Create an invoice with yesterday's due date for test
        InvoiceCreationRequest creationRequest = new InvoiceCreationRequest();
        creationRequest.setAmount(500.00);
        creationRequest.setDueDate(LocalDate.now().minusDays(1)); // Yesterday due
        invoiceService.createInvoice(creationRequest);

        InvoicePaymentRequest paymentRequest = new InvoicePaymentRequest();
        paymentRequest.setAmount(200.00);

        ResponseEntity<Object> response = invoiceService.doPayment(1, paymentRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testDoPaymentInvoiceNotFound() {
        InvoicePaymentRequest paymentRequest = new InvoicePaymentRequest();
        paymentRequest.setAmount(200.00);

        ResponseEntity<Object> response = invoiceService.doPayment(999, paymentRequest);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testProcessOverdue() {
        InvoiceCreationRequest creationRequest = new InvoiceCreationRequest();
        creationRequest.setAmount(500.00);
        creationRequest.setDueDate(LocalDate.now().minusDays(1)); // Yesterday
        invoiceService.createInvoice(creationRequest);

        OverdueProcessRequest overdueRequest = new OverdueProcessRequest();
        overdueRequest.setLateFee(50.00);
        overdueRequest.setOverdueDays(5);

        ResponseEntity<Object> response = invoiceService.processOverdue(overdueRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
