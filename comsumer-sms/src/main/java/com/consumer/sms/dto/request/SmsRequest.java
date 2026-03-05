package com.consumer.sms.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsRequest {
    private String messageId;
    private String keyword;
    private String sender;
    private String destination;
    private String partnerCode;
    private Date createdDate;
    private Date sendTime;
    private String status;
    private String description;

    private String shortMessage;
    private String encryptMessage;
    private Integer isEncrypt;
    private Integer type;
    private String requestTime;
    private String sercretKey;
}
