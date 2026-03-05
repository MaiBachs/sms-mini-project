package com.produce.sms.repository;

import com.produce.sms.entity.SmsTest;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SmsRepository extends JpaRepository<SmsTest, String> {
    boolean existsByMessageId(String messageId);
    List<SmsTest> findByStatus(String status);

    @Modifying
    @Transactional
    @Query("UPDATE SmsTest s SET s.status = :statusUpdate WHERE s.messageId = :messageId AND s.status = :status ")
    int claimSms(String messageId, String status, String statusUpdate);
}
