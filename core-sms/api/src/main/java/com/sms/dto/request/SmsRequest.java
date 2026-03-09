package com.sms.dto.request;

public class SmsRequest {
    private String messageId;
    private String keyword;
    private String sender;
    private String destination;
    private String shortMessage;
    private String partnerCode;

    // getters/setters
    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }
    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }
    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public String getShortMessage() { return shortMessage; }
    public void setShortMessage(String shortMessage) { this.shortMessage = shortMessage; }
    public String getPartnerCode() { return partnerCode; }
    public void setPartnerCode(String partnerCode) { this.partnerCode = partnerCode; }

    @Override
    public String toString() {
        return "SmsRequest{" +
                "messageId='" + messageId + '\'' +
                ", keyword='" + keyword + '\'' +
                ", sender='" + sender + '\'' +
                ", destination='" + destination + '\'' +
                ", shortMessage='" + shortMessage + '\'' +
                ", partnerCode='" + partnerCode + '\'' +
                '}';
    }
}
