package com.sms.service.impl;

import com.sms.dto.request.SmsRequest;
import com.sms.dto.response.ReportSmsResponse;
import com.sms.entity.SmsTest;
import com.sms.exception.BaseResponseException;
import com.sms.repository.SmsRepository;
import com.sms.service.SmsService;
import com.sms.util.Constant;
import com.sms.util.ErrorCode;
import com.sms.util.ErrorMessage;
import com.sms.validator.SmsValidate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class SmsServiceImpl implements SmsService {
    @Autowired
    private SmsRepository smsRepository;

    @Override
    public void receiverSmsApi(SmsRequest smsRequest) {
        log.info("=== Handle insert sms to db with method: receiverSmsApi ===");
        try {
            // validate request
            SmsValidate.validateSms(smsRequest);
            // validate exist messageId
            boolean existsByMessageId = smsRepository.existsByMessageId(smsRequest.getMessageId());
            if (existsByMessageId) {
                throw new BaseResponseException (ErrorCode.BAD_REQUEST, ErrorMessage.MESSAGE_ID_ALREADY_EXIST);
            }
            SmsTest smsTest = SmsTest.builder()
                    .messageId(smsRequest.getMessageId())
                    .keyword(smsRequest.getKeyword())
                    .sender(smsRequest.getSender())
                    .shortMessage(smsRequest.getShortMessage())
                    .destination(smsRequest.getDestination())
                    .partnerCode(smsRequest.getPartnerCode())
                    .createdDate(new Date())
                    .status(Constant.SmsStatus.NONE)
                    .build();

            smsRepository.save(smsTest);
            log.info("=== Handle insert sms success ===");
        } catch (BaseResponseException e) {
            log.info("Error: {}", e.getMessage());
            throw new BaseResponseException(e.getResponseStatusCode(), e.getMessage());
        }  catch (Exception e) {
            throw new BaseResponseException(ErrorCode.SYSTEM_ERROR);
        }
    }

    @Override
    public ReportSmsResponse reportSms() {
        return ReportSmsResponse.builder()
                .completed(smsRepository.countByStatus(Constant.SmsStatus.COMPLETE))
                .failed(smsRepository.countByStatus(Constant.SmsStatus.FAILED))
                .processing(smsRepository.countByStatus(Constant.SmsStatus.PROCESSING))
                .none(smsRepository.countByStatus(Constant.SmsStatus.NONE))
                .totalSms(smsRepository.count())
                .build();
    }
}
