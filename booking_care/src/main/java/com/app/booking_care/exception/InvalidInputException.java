package com.app.booking_care.exception;

import com.app.booking_care.model.ErrorModel;
import lombok.Getter;

@Getter
public class InvalidInputException extends AppException {
    private final ErrorModel errorModel;
    public InvalidInputException(ErrorModel errorModel, Object... args) {
        super(String.format(errorModel.getMessage(), args));
        this.errorModel = errorModel;
    }

    public InvalidInputException(ErrorModel errorModel, Throwable cause, Object... args) {
        super(String.format(errorModel.getMessage(), args), cause);
        this.errorModel = errorModel;
    }
}