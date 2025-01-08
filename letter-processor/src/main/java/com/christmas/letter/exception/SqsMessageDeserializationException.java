package com.christmas.letter.exception;

public class SqsMessageDeserializationException extends RuntimeException {
    public SqsMessageDeserializationException(String message) {
        super(message);
    }
}
