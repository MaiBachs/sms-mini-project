package com.sms.repository;

import com.sms.entity.SmsTest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmsRepository extends JpaRepository<SmsTest, String> {
    boolean existsByMessageId(String messageId);
    long countByStatus(String status);
}
