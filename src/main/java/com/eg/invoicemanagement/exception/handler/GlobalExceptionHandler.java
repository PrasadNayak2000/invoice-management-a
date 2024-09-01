package com.eg.invoicemanagement.exception.handler;

import com.eg.invoicemanagement.exception.ValidationException;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler({
            IllegalArgumentException.class,
            MethodArgumentTypeMismatchException.class,
            BindException.class
    })
    @ResponseBody
    protected ResponseEntity<Object> handleIllegalArgumentException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ex.getMessage());
    }

    @ExceptionHandler(value = {Throwable.class})
    @ResponseBody
    protected ResponseEntity<Object> handleConflict(Throwable ex) {
        log.error("GlobalExceptionHandling:{}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ResponseEntity<Object> onMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        val message = new StringBuilder();
        for (val fieldError : ex.getBindingResult().getFieldErrors()) {
            if (!message.isEmpty()) {
                message.append(", ");
            }
            message.append(fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage()
                    : fieldError.getField() + " is required");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(message);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    ResponseEntity<Object> validationException(String ex) {
        log.info("ValidationException : {}", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ex);
    }
}
