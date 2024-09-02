# Invoice Management

### The application consists below-mentioned functionalities

* Create Invoice:
    * This API method allows a client to create a new invoice.
    * When a POST request is made to the endpoint with a JSON body containing the invoice details such as amount and due
      date,
      the application processes this request to generate a new invoice.
    * The response will confirm whether the creation was successful or not.
    * Response contains the id of the created invoice in-case the request was successful.

* Get All Invoices:
    * This API method retrieves a list of all invoices stored in the system.
    * When a GET request is sent to this endpoint, the server responds with a collection of invoices,
      allowing the client to view all existing invoices at once.
    * This is useful for summarizing or displaying all invoice records.

* Do Payment:
    * This method processes a payment for a specific invoice.
    * It expects a PUT request that includes an invoice ID as a path variable and payment details in the request body.
    * The server updates the invoice record to reflect the payment and responds with the outcome of this transaction.
    * This method is crucial for managing payments and updating invoice statuses.

* Process Overdue:
    * This functionality will process all pending invoices that are overdue.
    * If an invoice is partially paid, the invoice should be marked as paid, and a new invoice should be
      created with the remaining amount plus the late fee, and a new overdue.
    * If an invoice is not paid at all, the invoice should be marked as void, a new invoice should be
      created with the amount plus the late fee, and a new overdue.

### Note

* Datasource autoconfiguration is disabled at main class and application memory is used for the storage of data.
* Models/Entities structure for table creation is pre-defined so that application can be enhanced to use database
  easily.
* The API specifications like endpoints, request and response formats are specified in a postman documentation and
  the link for the same is specified at the end of this file.

### Requirements

For building and running the application you need:

- https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html

### Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method
in the `com.eg.invoicemanagement.InvoiceManagementApplication` class from your IDE.
This will run the application on local port 8080.

### Running the application using Docker

Application can be run using docker container and access locally by exposing it to a local port.

### Steps to create docker container

* Run ```gradle clean``` and then ```gradle build``` command in the project workspace terminal.
* You can also perform gradle clean and build through your IDE instead of running above command.
* Once gradle build is completed, verify a jar ```invoice-management-0.0.1.jar``` will get generated.
  inside ```build/libs``` folder.
* Make sure docker-compose.yml file and Dockerfile exist in the root directory.
* Open the cmd from the root directory of the Spring Boot application.
* Run ```docker-compose up -d``` command.
* Running the above command downloads the jdk with the version specified at Dockerfile.
* A docker image and container gets created and all the endpoints are exposed to local port 8080.
* You can verify the availability of endpoints by making a GET request to ```http://localhost:8080/invoices```.

### APIs Collection Documentation is available at below link.

```https://documenter.getpostman.com/view/23412093/2sAXjM3X1m```
