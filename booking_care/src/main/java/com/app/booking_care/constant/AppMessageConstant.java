package com.app.booking_care.constant;

import com.app.booking_care.model.ErrorModel;
import org.springframework.http.HttpStatus;

public class AppMessageConstant {
    public static final ErrorModel ENTITY_NOT_FOUND = ErrorModel.of(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Entity not found");
    public static final ErrorModel INVALID_QUESTION_ID = ErrorModel.of(HttpStatus.BAD_REQUEST.value(), "Invalid Question IDs: %s do not belong to Test ID %s");
    public static final ErrorModel USER_NOT_FOUND = ErrorModel.of(HttpStatus.NOT_FOUND.value(), "User not found");
    public static final ErrorModel DUPLICATE_QUESTION_ID = ErrorModel.of(HttpStatus.BAD_REQUEST.value(), "Duplicate Question IDs found: %s");
}
