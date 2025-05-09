package com.app.booking_care.exception;

import com.app.booking_care.model.ErrorModel;
import lombok.Getter;

@Getter
public class AppException extends RuntimeException {

    private final ErrorModel errorModel;

    public AppException(ErrorModel errorModel) {
        super(errorModel.getMessage());
        this.errorModel = errorModel;
    }

    public AppException(ErrorModel errorModel, Throwable cause) {
        super(errorModel.getMessage(), cause);
        this.errorModel = errorModel;
    }

    public AppException(String message) {
        super(message);
        this.errorModel = null;
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
        this.errorModel = null;
    }

    public static AppException of(ErrorModel errorModel) {
        return new AppException(errorModel);
    }

    public static AppException of(ErrorModel errorModel, Throwable cause) {
        return new AppException(errorModel, cause);
    }

    public static AppException of(String message) {
        return new AppException(message);
    }

    public static AppException of(String message, Throwable cause) {
        return new AppException(message, cause);
    }
}