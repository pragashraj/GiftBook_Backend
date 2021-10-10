package com.giftbook.giftBook.exceptions;

public class FileNotFoundException extends Throwable{
    public FileNotFoundException(String message) {
        super(message);
    }

    public FileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
