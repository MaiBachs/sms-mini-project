package com.produce.sms.entity;

import java.util.Date;

public class SmsTest {
    private String messageId;
    private String keyword;
    private String sender;
    private String shortMessage;
    private String destination;
    private String partnerCode;
    private Date createdDate;
    private Date sendTime;
    private String status;
    private String description;

    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }
    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }
    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }
    public String getShortMessage() { return shortMessage; }
    public void setShortMessage(String shortMessage) { this.shortMessage = shortMessage; }
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
}
