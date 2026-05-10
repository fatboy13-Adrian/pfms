package com.app.pfms.Exceptions.Expense;

public class ExpenseNotFoundException extends RuntimeException {
	public ExpenseNotFoundException(String message) {
		super(message);
	}
}