package com.produce.sms.service.impl;

import com.produce.sms.entity.SmsTest;
import com.produce.sms.repository.SmsRepository;
import com.produce.sms.service.SmsService;
import com.produce.sms.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmsServiceImpl implements SmsService {
    @Autowired
    private SmsRepository smsRepository;

    @Override
    public List<SmsTest> getListSmsNoRead() {
        return smsRepository.findByStatus(Constant.SmsStatus.NONE);
    }
}
