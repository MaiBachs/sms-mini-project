package com.sms.dto.response.factory;

import com.sms.dto.response.GeneralResponse;

public class ResponseFactory {
    public static <T> GeneralResponse<T> success(GeneralResponse<T> resp) {
        return resp;
    }
}
