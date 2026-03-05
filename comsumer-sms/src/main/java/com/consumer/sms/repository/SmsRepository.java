package com.consumer.sms.repository;

import com.consumer.sms.entity.SmsTest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmsRepository extends JpaRepository<SmsTest, String> {
}
