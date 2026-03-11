package com.consumer.sms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "SMS_TEST")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SmsTest {

    @Id
    @Column(name = "message_id")
    private String messageId;

    @Column(name = "keyword")
    private String keyword;

    @Column(name = "sender")
    private String sender;

    @Column(name = "short_message")
    private String shortMessage;

    @Column(name = "destination")
    private String destination;

    @Column(name = "partner_code")
    private String partnerCode;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "send_time")
    private Date sendTime;

    @Column(name = "status")
    private String status;

    @Column(name = "description")
    private String description;
}
