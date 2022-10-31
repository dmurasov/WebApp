package net.homework.webapp.exception;

public class DepartmentNotFoundException extends ResponseNotFoundException{
    public DepartmentNotFoundException(String message) {
        super(message);
    }
}
