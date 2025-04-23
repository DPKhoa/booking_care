package com.app.booking_care.exception;

import com.app.booking_care.model.ErrorModel;
import lombok.Data;

@Data
public class AppException extends RuntimeException {
    private ErrorModel errorModel;

    public AppException(ErrorModel errorModel){
        super(errorModel.getMessage());
        this.errorModel = errorModel;
    }
    public AppException(String message){
        super(message);
        this.errorModel = null;
    }
    public static AppException of(ErrorModel errorModel){return  new AppException(errorModel);}
    public static AppException of(String message ){return  new AppException(message);}
}
