package com.sms.service.impl;

import com.sms.dto.request.SmsRequest;
import com.sms.dto.response.ReportSmsResponse;
import com.sms.entity.SmsTest;
import com.sms.repository.SmsRepository;
import com.sms.service.SmsService;
import com.sms.util.Constant;
import com.sms.validator.SmsValidate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

public class SmsServiceImpl implements SmsService {
    private static final Logger log = LogManager.getLogger(SmsServiceImpl.class);
    private final SmsRepository smsRepository = new SmsRepository();

    @Override
    public void receiverSmsApi(SmsRequest smsRequest) {
        log.info("=== Handle insert sms to db with method: receiverSmsApi ===");
        SmsValidate.validateSms(smsRequest);
        boolean exists = smsRepository.existsByMessageId(smsRequest.getMessageId());
        if (exists) {
            throw new RuntimeException("MESSAGE_ID_ALREADY_EXIST");
        }
        SmsTest smsTest = new SmsTest();
        smsTest.setMessageId(smsRequest.getMessageId());
        smsTest.setKeyword(smsRequest.getKeyword());
        smsTest.setSender(smsRequest.getSender());
        smsTest.setShortMessage(smsRequest.getShortMessage());
        smsTest.setDestination(smsRequest.getDestination());
        smsTest.setPartnerCode(smsRequest.getPartnerCode());
        smsTest.setCreatedDate(new Date());
        smsTest.setStatus(Constant.SmsStatus.NONE);
        smsRepository.save(smsTest);
    }

    @Override
    public ReportSmsResponse reportSms() {
        ReportSmsResponse resp = new ReportSmsResponse();
        resp.setCompleted(smsRepository.countByStatus(Constant.SmsStatus.COMPLETE));
        resp.setFailed(smsRepository.countByStatus(Constant.SmsStatus.FAILED));
        resp.setProcessing(smsRepository.countByStatus(Constant.SmsStatus.PROCESSING));
        resp.setNone(smsRepository.countByStatus(Constant.SmsStatus.NONE));
        resp.setTotalSms(smsRepository.count());
        return resp;
    }
}
