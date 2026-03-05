package com.sms.exception;

import com.sms.dto.response.GeneralResponse;
import com.sms.dto.response.factory.ResponseFactory;
import com.sms.util.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private final ResponseFactory factory;

    @ExceptionHandler({BaseResponseException.class})
    public ResponseEntity<?> handlerException(BaseResponseException ex) {
        try {
            return factory.failure(new GeneralResponse<>(), ex.getResponseStatusCode(), ex.getMessage());
        } catch (Exception e) {
            return factory.failure(ErrorCode.SYSTEM_ERROR, ex.getMessage());
        }
    }

    @ExceptionHandler({Exception.class, RuntimeException.class})
    public ResponseEntity<?> handlerOther(Exception ex) {
        return factory.failure(ErrorCode.SYSTEM_ERROR, ex.getMessage());
    }
}
