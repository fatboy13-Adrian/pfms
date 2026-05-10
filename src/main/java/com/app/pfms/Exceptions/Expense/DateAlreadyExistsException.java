package com.app.pfms.Exceptions.Expense;
import java.time.LocalDate;

public class DateAlreadyExistsException extends RuntimeException {
    public DateAlreadyExistsException(LocalDate date) {
        super("Expense already exists for date: " + date);
    }
}