package com.giftbook.giftBook.exceptions;

public class FileStorageException extends Throwable{
    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileStorageException(String message) {
        super(message);
    }
}
