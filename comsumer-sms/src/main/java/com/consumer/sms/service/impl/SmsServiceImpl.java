package com.consumer.sms.service.impl;

import com.consumer.sms.dto.request.SmsRequest;
import com.consumer.sms.dto.response.SmsResponse;
import com.consumer.sms.entity.SmsTest;
import com.consumer.sms.feign.SmsFeignClient;
import com.consumer.sms.repository.SmsRepository;
import com.consumer.sms.service.SmsService;
import com.consumer.sms.util.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsServiceImpl implements SmsService {
    private final SmsFeignClient smsFeignClient;
    @Autowired
    private SmsRepository smsRepository;

    @Override
    @Retryable(
            value = {
                    feign.RetryableException.class,
                    java.net.SocketTimeoutException.class,
                    RuntimeException.class
            },
            maxAttemptsExpression = "#{${sms.retry.max-attempts}}",
            backoff = @Backoff(
                    delayExpression = "#{${sms.retry.delay}}"
            )
    )
    public SmsResponse sendWithRetryAndUpdateStatusSms(SmsRequest sms) {
        SmsTest smsTest = smsRepository.findById(sms.getMessageId()).orElseThrow(NullPointerException::new);
        SmsResponse response = null;
        try {
            response = smsFeignClient.sendSms(sms);
            smsTest.setStatus(Constant.SmsStatus.COMPLETE);
            smsRepository.save(smsTest);
        } catch (feign.RetryableException e) {
            log.info("=== Retry send sms to gateway ===");
            smsTest.setStatus(Constant.SmsStatus.FAILED);
            smsRepository.save(smsTest);
            throw new RuntimeException("Gateway returned failed status");
        } catch (Exception e) {
            log.error("System error");
        }
        return response;
    }
}
