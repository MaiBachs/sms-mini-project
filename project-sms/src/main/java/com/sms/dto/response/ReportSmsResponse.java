package com.sms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportSmsResponse {
    private Long completed;
    private Long processing;
    private Long failed;
    private Long none;
    private Long totalSms;
}
