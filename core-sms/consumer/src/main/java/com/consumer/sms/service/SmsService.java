package com.consumer.sms.service;

import com.consumer.sms.dto.SmsRequest;
import com.consumer.sms.dto.SmsResponse;

public interface SmsService {
    SmsResponse sendWithRetryAndUpdateStatusSms(SmsRequest sms) throws Exception;
}
