package com.produce.sms.repository;

import com.produce.sms.entity.SmsTest;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface SmsRepository extends JpaRepository<SmsTest, String> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE sms_test SET status = 'PROCESSING' " +
            "WHERE message_id IN (SELECT message_id FROM sms_test " +
            "WHERE status = :status ORDER BY created_date LIMIT 2 FOR UPDATE SKIP LOCKED ) " +
            "RETURNING *", nativeQuery = true)
    List<SmsTest> getListSmsNoReadAndClaim(String status);
}
