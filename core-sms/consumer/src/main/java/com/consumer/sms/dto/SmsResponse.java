package com.consumer.sms.dto;

public class SmsResponse {
    private String messageId;
    private String status;
    private String description;
    private String isMnp;
    private String partnerCode;
    private String sender;
    private String keyword;
    private String destination;

    public SmsResponse() {}

    @Override
    public String toString() {
        return "SmsResponse{" +
                "messageId='" + messageId + '\'' +
                ", status='" + status + '\'' +
                ", description='" + description + '\'' +
                ", isMnp='" + isMnp + '\'' +
                ", partnerCode='" + partnerCode + '\'' +
                ", sender='" + sender + '\'' +
                ", keyword='" + keyword + '\'' +
                ", destination='" + destination + '\'' +
                '}';
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsMnp() {
        return isMnp;
    }

    public void setIsMnp(String isMnp) {
        this.isMnp = isMnp;
    }

    public String getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
