package com.eg.invoicemanagement.service.impl;

import com.eg.invoicemanagement.dto.request.InvoiceCreationRequest;
import com.eg.invoicemanagement.dto.request.InvoicePaymentRequest;
import com.eg.invoicemanagement.dto.request.OverdueProcessRequest;
import com.eg.invoicemanagement.dto.response.InvoiceResponse;
import com.eg.invoicemanagement.model.Invoice;
import com.eg.invoicemanagement.model.enums.Status;
import com.eg.invoicemanagement.service.InvoiceService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.eg.invoicemanagement.model.enums.Status.PENDING;

@Service
@Log4j2
public class InvoiceServiceImpl implements InvoiceService {

    private final List<Invoice> invoices = new ArrayList<>();

    @Override
    public ResponseEntity<Object> createInvoice(InvoiceCreationRequest request) {
        Invoice invoice = new Invoice();
        invoice.setId(getLastInvoiceId() + 1);
        invoice.setAmount(request.getAmount());
        invoice.setDueDate(request.getDueDate());
        invoice.setStatus(PENDING);
        invoices.add(invoice);
        log.info("Invoice created with id {}", invoice.getId());
        return new ResponseEntity<>(new InvoiceResponse(invoice.getId()), HttpStatus.CREATED);
    }

    //This method returns the maximum/last generated invoice id
    private int getLastInvoiceId() {
        return invoices.stream().map(Invoice::getId).mapToInt(id -> id).max().orElse(0);
    }

    @Override
    public ResponseEntity<Object> getAllInvoices() {
        val response = invoices.stream().map(this::getResponse).toList();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private InvoiceResponse getResponse(Invoice invoice) {
        InvoiceResponse response = new InvoiceResponse();
        BeanUtils.copyProperties(invoice, response);
        response.setStatus(invoice.getStatus().getValue());
        return response;
    }

    @Override
    public ResponseEntity<Object> doPayment(Integer invoiceId, InvoicePaymentRequest request) {
        val invoiceOpt = invoices.stream().filter(inv -> invoiceId.equals(inv.getId())).findAny();
        //Checking whether invoice exists with given invoiceId
        if (invoiceOpt.isEmpty())
            return new ResponseEntity<>("No invoice found with id " + invoiceId, HttpStatus.NOT_FOUND);

        Invoice invoice = invoiceOpt.get();

        //Restricting payment if due date is already passed
        if (invoice.getDueDate().isBefore(LocalDate.now()))
            return new ResponseEntity<>("Invoice due date is already over", HttpStatus.BAD_REQUEST);

        double invoiceAmount = invoice.getAmount();
        double paidAmount = invoice.getPaidAmount();
        double pendingAmount = invoiceAmount - paidAmount;
        double paymentAmount = request.getAmount();

        //Checking if payment amount exceeds the invoice pending amount
        if (paymentAmount > pendingAmount)
            return new ResponseEntity<>("Total Payment amount exceeds invoice amount " + invoiceAmount +
                    " (" + paidAmount + " paid already)", HttpStatus.BAD_REQUEST);

        invoice.setPaidAmount(paidAmount + paymentAmount);
        if (paymentAmount == pendingAmount)
            invoice.setStatus(Status.PAID);
        log.info("Invoice payment of {} for invoice id {} is successful", paymentAmount, invoiceId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> processOverdue(OverdueProcessRequest request) {
        if (invoices.isEmpty())
            return new ResponseEntity<>(HttpStatus.OK);

        double lateFee = request.getLateFee();
        int overdueDays = request.getOverdueDays();

        //For storing newly created invoices
        List<Invoice> newInvoices = new ArrayList<>();

        //Update status of each invoice and create a new invoice for each
        for (Invoice invoice : invoices) {
            if (!PENDING.equals(invoice.getStatus()) || invoice.getDueDate().isEqual(LocalDate.now()) ||
                    invoice.getDueDate().isAfter(LocalDate.now()))
                continue;
            double amount = invoice.getAmount();
            double paidAmount = invoice.getPaidAmount();

            //If invoice is partially paid, mark status as 'paid' else mark as 'void' if not at all paid
            if (paidAmount > 0)
                invoice.setStatus(Status.PAID);
            else
                invoice.setStatus(Status.VOID);

            //Create a new invoice with amount (remaining amount + late fee) and a new overdue
            Invoice newInvoice = new Invoice();
            newInvoice.setId(getLastInvoiceId() + 1);
            newInvoice.setAmount((amount - paidAmount) + lateFee);
            newInvoice.setDueDate(invoice.getDueDate().plusDays(overdueDays));
            newInvoice.setStatus(PENDING);
            newInvoices.add(newInvoice);
        }
        invoices.addAll(newInvoices);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}