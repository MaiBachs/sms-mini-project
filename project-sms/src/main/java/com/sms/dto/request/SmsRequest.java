package com.sms.dto.request;

import lombok.Data;

@Data
public class SmsRequest {
    @Override
    public String toString() {
        return "SmsRequest{" +
                "messageId='" + messageId + '\'' +
                ", keyword='" + keyword + '\'' +
                ", sender='" + sender + '\'' +
                ", shortMessage='" + shortMessage + '\'' +
                ", destination='" + destination + '\'' +
                ", partnerCode='" + partnerCode + '\'' +
                '}';
    }

    private String messageId;
    private String keyword;
    private String sender;
    private String shortMessage;
    private String destination;
    private String partnerCode;
}
