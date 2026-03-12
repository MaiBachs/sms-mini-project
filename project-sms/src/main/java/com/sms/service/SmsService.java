package com.sms.service;

import com.sms.dto.request.SmsRequest;
import com.sms.dto.response.ReportSmsResponse;

public interface SmsService {
    void receiverSmsApi (SmsRequest smsRequest);
    ReportSmsResponse reportSms();

    /**
     * Build a CSV report suitable for downloading in Excel.
     */
    byte[] exportReportCsv();
}
