package com.consumer.sms.service;

import com.consumer.sms.dto.request.SmsRequest;
import com.consumer.sms.dto.response.SmsResponse;

public interface SmsService {
    SmsResponse sendWithRetryAndUpdateStatusSms(SmsRequest sms);
}
