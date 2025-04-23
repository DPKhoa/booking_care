package com.app.booking_care.constant;

import com.app.booking_care.model.ErrorModel;
import org.springframework.http.HttpStatus;

public class AppMessageConstant {
    public static ErrorModel ENTITY_NOT_FOUND = ErrorModel.of(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Entity not found");

}
