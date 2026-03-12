package com.sms.service.impl;

import com.sms.dto.request.SmsRequest;
import com.sms.dto.response.ReportSmsResponse;
import com.sms.dto.response.SmsTestReport;
import com.sms.entity.SmsTest;
import com.sms.repository.SmsRepository;
import com.sms.service.SmsService;
import com.sms.util.Constant;
import com.sms.validator.SmsValidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class SmsServiceImpl implements SmsService {
    private static final Logger log = LoggerFactory.getLogger(SmsServiceImpl.class);
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

    @Override
    public byte[] exportReportCsv() {
        List<SmsTestReport> list = smsRepository.findAll();
        // simple CSV with header row
        String header = "messageId,keyword,sender,shortMessage,destination,partnerCode,createdDate,sendTime,status,description";
        StringBuilder sb = new StringBuilder("\uFEFF").append(header).append('\n');
        for (SmsTestReport s : list) {
            // escape double quotes
            sb.append(escapeCsv(s.getMessageId())).append(',');
            sb.append(escapeCsv(s.getKeyword())).append(',');
            sb.append(escapeCsv(s.getSender())).append(',');
            sb.append(escapeCsv(s.getShortMessage())).append(',');
            sb.append(escapeCsv(s.getDestination())).append(',');
            sb.append(escapeCsv(s.getPartnerCode())).append(',');
            sb.append(s.getCreatedDate() != null ? s.getCreatedDate() : "").append(',');
            sb.append(s.getSendTime() != null ? s.getSendTime() : "").append(',');
            sb.append(escapeCsv(s.getStatus())).append(',');
            sb.append(escapeCsv(s.getDescription())).append('\n');
        }
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        String v = value.replace("\"", "\"\"");
        if (v.contains(",") || v.contains("\"")) {
            return "\"" + v + "\"";
        }
        return v;
    }
}
