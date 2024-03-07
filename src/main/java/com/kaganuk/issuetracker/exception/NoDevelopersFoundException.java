package com.kaganuk.issuetracker.exception;

public class NoDevelopersFoundException extends RuntimeException{
    public NoDevelopersFoundException() {
        super("No developers found for planning issues");
    }
}
