package com.consumer.sms.service.impl;

import com.consumer.sms.dto.SmsRequest;
import com.consumer.sms.dto.SmsResponse;
import com.consumer.sms.entity.SmsTest;
import com.consumer.sms.repository.SmsRepository;
import com.consumer.sms.service.SmsService;
import com.consumer.sms.util.Constant;
import com.consumer.sms.util.HttpClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmsServiceImpl implements SmsService {
    private static final Logger log = LoggerFactory.getLogger(SmsServiceImpl.class);
    private final SmsRepository smsRepository = new SmsRepository();
    private final String urlGateway = "https://smsgw.vnpaytest.vn/smsgw/sendSms";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public SmsResponse sendWithRetryAndUpdateStatusSms(SmsRequest sms) throws Exception {
        SmsTest smsTest = smsRepository.findById(sms.getMessageId());
        if (smsTest == null) {
            log.error("Not found sms in database");
            throw new RuntimeException();
        }
        SmsResponse response = null;
        log.info("=== Send sms and retry ===");
        for (int i = 0; i <= 3; i++) {
            try {
                String smsJson = objectMapper.writeValueAsString(sms);
                String responseStr = HttpClient.postJson(urlGateway, smsJson);
                response = objectMapper.readValue(responseStr, SmsResponse.class);
                smsTest.setStatus(Constant.SmsStatus.COMPLETE);
                smsRepository.updateStatusAndSendTime(smsTest.getMessageId(), Constant.SmsStatus.COMPLETE);
                break;
            } catch (Exception e) {
                log.error("=== Error: Retry send sms to gateway ===");
                smsRepository.updateStatusAndSendTime(smsTest.getMessageId(), Constant.SmsStatus.FAILED);
            }
        }
        return response;
    }
}
