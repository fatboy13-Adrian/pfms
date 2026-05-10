package com.app.pfms.Exceptions.Budget;
import java.time.YearMonth;

public class MonthNotFoundException extends RuntimeException {
    public MonthNotFoundException(YearMonth month) {
        super("Budget not found: " + month);
    }
}