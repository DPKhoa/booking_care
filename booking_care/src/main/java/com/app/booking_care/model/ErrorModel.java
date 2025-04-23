package com.app.booking_care.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ErrorModel implements Serializable {
    private int statusCode;
    private String message;

    public static  ErrorModel of(int statusCode, String message ) {
        return new ErrorModel(statusCode, message);
    }
    public static  ErrorModel of(int statusCode) {
        return new ErrorModel(statusCode,"");
    }
}
