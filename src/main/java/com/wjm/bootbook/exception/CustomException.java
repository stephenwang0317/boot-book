package com.wjm.bootbook.exception;

/**
 * @author stephen wang
 */
public class CustomException extends RuntimeException{
    public CustomException(String message) {
        super(message);
    }
}
