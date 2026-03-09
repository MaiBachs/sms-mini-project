package com.produce.sms.service.impl;

import com.produce.sms.entity.SmsTest;
import com.produce.sms.repository.SmsRepository;
import com.produce.sms.service.SmsService;
import com.produce.sms.util.Constant;

import java.util.List;

public class SmsServiceImpl implements SmsService {
    private final SmsRepository smsRepository = new SmsRepository();

    @Override
    public List<SmsTest> getListSmsNoRead() {
        return smsRepository.findByStatus(Constant.SmsStatus.NONE);
    }
}
