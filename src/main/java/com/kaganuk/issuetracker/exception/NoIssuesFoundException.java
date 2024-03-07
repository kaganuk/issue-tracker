package com.kaganuk.issuetracker.exception;

public class NoIssuesFoundException extends RuntimeException{
    public NoIssuesFoundException() {
        super("No issues found for planning issues");
    }
}
