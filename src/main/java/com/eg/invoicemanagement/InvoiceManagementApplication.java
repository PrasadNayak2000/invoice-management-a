package com.eg.invoicemanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication //(exclude = {DataSourceAutoConfiguration.class})
public class InvoiceManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(InvoiceManagementApplication.class, args);
    }

}
