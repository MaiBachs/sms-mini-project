package com.sms.exception;

import com.sms.dto.response.ResponseStatusCode;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@EqualsAndHashCode(callSuper = true)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseResponseException extends RuntimeException {
    ResponseStatusCode responseStatusCode;

    public BaseResponseException(ResponseStatusCode responseStatusCode) {
        this.responseStatusCode = responseStatusCode;
    }

    public BaseResponseException(ResponseStatusCode responseStatusCode, String message) {
        super(message);
        this.responseStatusCode = responseStatusCode;
    }
}
