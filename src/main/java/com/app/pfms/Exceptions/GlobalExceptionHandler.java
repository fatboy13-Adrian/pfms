package com.app.pfms.Exceptions;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.app.pfms.Exceptions.Budget.BudgetNotFoundException;
import com.app.pfms.Exceptions.Budget.MonthAlreadyExistsException;
import com.app.pfms.Exceptions.Budget.MonthNotFoundException;
import com.app.pfms.Exceptions.Expense.DateAlreadyExistsException;
import com.app.pfms.Exceptions.Expense.DateNotFoundException;
import com.app.pfms.Exceptions.Expense.ExpenseNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private ResponseEntity<Object> buildResponse(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(MonthNotFoundException.class)
    public ResponseEntity<Object> handleMonthNotFound(MonthNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(MonthAlreadyExistsException.class)
    public ResponseEntity<Object> handleMonthAlreadyExists(MonthAlreadyExistsException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(BudgetNotFoundException.class)
    public ResponseEntity<Object> handleBudgetNotFound(BudgetNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(ExpenseNotFoundException.class)
    public ResponseEntity<Object> handleExpenseNotFound(ExpenseNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(DateNotFoundException.class)
    public ResponseEntity<Object> handleDateNotFound(DateNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(DateAlreadyExistsException.class)
    public ResponseEntity<Object> handleDateAlreadyExists(DateAlreadyExistsException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(ExportExcelFailedException.class)
    public ResponseEntity<Object> handleExportExcelFailed(ExportExcelFailedException ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneric(Exception ex) {
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Unexpected error: " + ex.getMessage()
        );
    }
}