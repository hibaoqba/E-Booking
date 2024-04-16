package com.example.demo.exceptions;

public class IncorrectPasswordException extends RuntimeException{
    public IncorrectPasswordException() {
        super("Le mot de passe actuel est incorrect.");
    }

    public IncorrectPasswordException(String message) {
        super(message);
    }

    public IncorrectPasswordException(String message, Throwable cause) {
        super(message, cause);
    }
}
