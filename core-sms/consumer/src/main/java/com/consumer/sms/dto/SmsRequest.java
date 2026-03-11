package com.consumer.sms.dto;

import java.util.Date;

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

    public SmsRequest() {}

    @Override
    public String toString() {
        return "SmsRequest{" +
                "messageId='" + messageId + '\'' +
                ", keyword='" + keyword + '\'' +
                ", sender='" + sender + '\'' +
                ", destination='" + destination + '\'' +
                ", partnerCode='" + partnerCode + '\'' +
                ", createdDate=" + createdDate +
                ", sendTime=" + sendTime +
                ", status='" + status + '\'' +
                ", description='" + description + '\'' +
                ", shortMessage='" + shortMessage + '\'' +
                ", encryptMessage='" + encryptMessage + '\'' +
                ", isEncrypt=" + isEncrypt +
                ", type=" + type +
                ", requestTime='" + requestTime + '\'' +
                ", sercretKey='" + sercretKey + '\'' +
                '}';
    }

    // getters and setters
    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }
    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }
    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public String getPartnerCode() { return partnerCode; }
    public void setPartnerCode(String partnerCode) { this.partnerCode = partnerCode; }
    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }
    public Date getSendTime() { return sendTime; }
    public void setSendTime(Date sendTime) { this.sendTime = sendTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getShortMessage() { return shortMessage; }
    public void setShortMessage(String shortMessage) { this.shortMessage = shortMessage; }
    public String getEncryptMessage() { return encryptMessage; }
    public void setEncryptMessage(String encryptMessage) { this.encryptMessage = encryptMessage; }
    public Integer getIsEncrypt() { return isEncrypt; }
    public void setIsEncrypt(Integer isEncrypt) { this.isEncrypt = isEncrypt; }
    public Integer getType() { return type; }
    public void setType(Integer type) { this.type = type; }
    public String getRequestTime() { return requestTime; }
    public void setRequestTime(String requestTime) { this.requestTime = requestTime; }
    public String getSercretKey() { return sercretKey; }
    public void setSercretKey(String sercretKey) { this.sercretKey = sercretKey; }
}
