package com.eg.invoicemanagement.controller;

import com.eg.invoicemanagement.dto.request.InvoiceCreationRequest;
import com.eg.invoicemanagement.dto.request.InvoicePaymentRequest;
import com.eg.invoicemanagement.dto.request.OverdueProcessRequest;
import com.eg.invoicemanagement.dto.response.InvoiceResponse;
import com.eg.invoicemanagement.service.InvoiceService;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class InvoiceControllerTest {

    @Mock
    private InvoiceService invoiceService;

    protected MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testCreateInvoice() throws Exception {
        InvoiceCreationRequest request = new InvoiceCreationRequest();
        request.setAmount(1000.00);
        request.setDueDate(LocalDate.of(2024, 9, 2));

        Mockito.when(invoiceService.createInvoice(request)).thenReturn(new ResponseEntity<>(new InvoiceResponse(1),
                HttpStatus.CREATED));

        mockMvc.perform(post("/invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":1000.00,\"due_date\":\"2024-09-02\"}"))
                .andExpect(status().isCreated());
    }

    //Invalid request
    @Test
    void testCreateInvoiceFailure() throws Exception {
        InvoiceCreationRequest request = new InvoiceCreationRequest();
        request.setAmount(1000.00);
        request.setDueDate(LocalDate.of(2024, 9, 2));

        Mockito.when(invoiceService.createInvoice(request)).thenReturn(new ResponseEntity<>(new InvoiceResponse(1),
                HttpStatus.CREATED));

        mockMvc.perform(post("/invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":1000.00}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAllInvoices() throws Exception {
        mockMvc.perform(get("/invoices"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(0));
    }

    @Test
    void testDoPayment() throws Exception {
        InvoicePaymentRequest request = new InvoicePaymentRequest();
        request.setAmount(200.00);

        mockMvc.perform(put("/invoices/1/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":200.00}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDoPaymentFailure() throws Exception {
        InvoicePaymentRequest request = new InvoicePaymentRequest();
        request.setAmount(200.00);

        when(invoiceService.doPayment(1, request)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(put("/invoices/1/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amoun\":200.00}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testProcessOverdue() throws Exception {
        OverdueProcessRequest request = new OverdueProcessRequest();
        request.setLateFee(50.00);
        request.setOverdueDays(5);

        when(invoiceService.processOverdue(request)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(put("/invoices/process-overdue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"late_fee\":50.00,\"overdue_days\":5}"))
                .andExpect(status().isOk());
    }

    //Invalid request
    @Test
    void testProcessOverdueFailure() throws Exception {
        OverdueProcessRequest request = new OverdueProcessRequest();
        request.setLateFee(50.00);
        request.setOverdueDays(5);

        when(invoiceService.processOverdue(request)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(put("/invoices/process-overdue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"overdue_days\":5}"))
                .andExpect(status().isBadRequest());
    }
}
