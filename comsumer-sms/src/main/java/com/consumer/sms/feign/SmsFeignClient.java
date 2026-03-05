package com.consumer.sms.feign;

import com.consumer.sms.dto.request.SmsRequest;
import com.consumer.sms.dto.response.SmsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "smsGateway",
        url = "https://smsgw.vnpaytest.vn"
)
public interface SmsFeignClient {
    @PostMapping(
            value = "/smsgw/sendSms",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    SmsResponse sendSms(@RequestBody SmsRequest smsList);
}
