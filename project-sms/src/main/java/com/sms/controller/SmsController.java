package com.sms.controller;

import com.sms.dto.request.SmsRequest;
import com.sms.dto.response.GeneralResponse;
import com.sms.dto.response.factory.ResponseFactory;
import com.sms.dto.response.sms.SmsResponse;
import com.sms.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/sms")
@Slf4j
public class SmsController {
    @Autowired
    private SmsService service;
    private final ResponseFactory responseFactory;

    public SmsController(ResponseFactory responseFactory) {
        this.responseFactory = responseFactory;
    }

    @PostMapping("")
    public ResponseEntity<GeneralResponse<SmsResponse>> receiverSmsApi (@RequestBody SmsRequest smsRequest) {
        log.info("=== Receive sms to api: {} ===", smsRequest.toString() );
        service.receiverSmsApi(smsRequest);
        return responseFactory.success(new GeneralResponse<>());
    }

    @GetMapping("/report")
    public ResponseEntity<GeneralResponse<SmsResponse>> reportSms () {
        log.info("=== Report sms api ===");
        GeneralResponse response = new GeneralResponse();
        response.setData(service.reportSms());
        return responseFactory.success(response);
    }

    @GetMapping("/report/export")
    public ResponseEntity<byte[]> exportReport() {
        log.info("=== Export report csv ===");
        byte[] csv = service.exportReportCsv();
        return ResponseEntity.ok()
                .header("Content-Type", "text/csv; charset=UTF-8")
                .header("Content-Disposition", "attachment; filename=\"report.csv\"")
                .body(csv);
    }
}
