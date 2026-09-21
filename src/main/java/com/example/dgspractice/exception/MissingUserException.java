package com.example.dgspractice.exception;

public class MissingUserException extends RuntimeException {

    public MissingUserException() {
        super("This operation requires a caller identity: send the X-User header");
    }
}
