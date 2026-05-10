package com.app.pfms.Exceptions;

public class ExportExcelFailedException extends RuntimeException {
	public ExportExcelFailedException(String message, Throwable cause) {
        super(message);
    }
}