package net.homework.webapp.exception;

public class EmployeeNotFoundException extends ResponseNotFoundException {
    public EmployeeNotFoundException(String message) {
        super(message);
    }
}