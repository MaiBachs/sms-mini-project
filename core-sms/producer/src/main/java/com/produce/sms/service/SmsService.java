package com.produce.sms.service;

import com.produce.sms.entity.SmsTest;

import java.util.List;

public interface SmsService {
    List<SmsTest> getListSmsNoReadAndClaim();
}
