package com.encryption_service.exception;

/**
 * Author: Motuma Gishu, Senior Software Engineer
 * Date: 12/16/24
 * Description: CustomException
 */
public class CustomException extends RuntimeException {
    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }
}
