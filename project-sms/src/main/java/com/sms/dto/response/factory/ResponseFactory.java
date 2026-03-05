package com.sms.dto.response.factory;

import com.sms.dto.response.GeneralResponse;
import com.sms.dto.response.ResponseStatus;
import com.sms.dto.response.ResponseStatusCode;
import com.sms.util.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

@Component
public class ResponseFactory {
    public <T>ResponseEntity<GeneralResponse<T>> success(T data) {
        GeneralResponse<T> response = new GeneralResponse<>();
        response.setData(data);
        return success(response);
    }

    public <T> ResponseEntity<GeneralResponse<T>> success(GeneralResponse<T> response) {
        response.setStatus(getStatus(ErrorCode.SUCCESS));
        return ResponseEntity.ok().body(response);
    }

    public <T> ResponseEntity<GeneralResponse<T>> failure(ResponseStatusCode statusCode, String message) {
        GeneralResponse<T> response = new GeneralResponse<>();
        return failure(response, statusCode, message);
    }

    public <T> ResponseEntity<GeneralResponse<T>> failure(GeneralResponse<T> response, ResponseStatusCode status, String message) {
        if (Objects.isNull(response)) {
            response = new GeneralResponse<T>();
        }
        response.setStatus(getStatus(status, message));
        return ResponseEntity.status(status.getHttpCode()).body(response);
    }

    private ResponseStatus getStatus(ResponseStatusCode statusCode) {
        return new ResponseStatus(
                statusCode.getCode(), statusCode.getCode(),
                new Date(), statusCode.getCode());
    }

    private ResponseStatus getStatus(ResponseStatusCode statusCode, String message) {
        return new ResponseStatus(
                statusCode.getCode(), message,
                new Date(), statusCode.getCode());
    }
}
