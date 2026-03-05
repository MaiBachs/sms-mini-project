package com.sms.util;

import com.sms.dto.response.ResponseStatusCode;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ErrorCode {
    public static final ResponseStatusCode SUCCESS = new ResponseStatusCode(StatusCode.SUCCESS, 200);
    public static final ResponseStatusCode BAD_REQUEST = new ResponseStatusCode(StatusCode.BAD_REQUEST, 400);
    public static final ResponseStatusCode INVALID_TOKEN = new ResponseStatusCode(StatusCode.UN_AUTHENTICATED, 401);
    public static final ResponseStatusCode NOT_FOUND = new ResponseStatusCode(StatusCode.NOT_FOUND, 404);
    public static final ResponseStatusCode NOT_PERMISSION = new ResponseStatusCode(StatusCode.NOT_PERMIT, 403);
    public static final ResponseStatusCode SYSTEM_ERROR = new ResponseStatusCode(StatusCode.SYSTEM_ERROR, 500);

}
