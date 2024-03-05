package com.kaganuk.issuetracker.exception;

public class NoSuchEntityException extends RuntimeException{
    public NoSuchEntityException(String entityName, Integer id) {
        super(entityName + " not found with ID of: " + id);
    }
}
