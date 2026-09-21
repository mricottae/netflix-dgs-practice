package com.example.dgspractice.exception;

public class ShowNotFoundException extends RuntimeException {
    public ShowNotFoundException(int id) {
        super("No show found with id " + id);
    }
}
