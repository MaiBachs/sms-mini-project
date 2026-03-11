package com.consumer.sms.repository;

import com.consumer.sms.entity.SmsTest;
import com.consumer.sms.config.DbPool;

import java.sql.*;

public class SmsRepository {
    public SmsTest findById(String messageId) {
        String sql = "SELECT * FROM sms_test WHERE message_id = ?";
        try (Connection conn = DbPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, messageId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    SmsTest s = new SmsTest();
                    s.setMessageId(rs.getString("message_id"));
                    s.setKeyword(rs.getString("keyword"));
                    s.setSender(rs.getString("sender"));
                    s.setShortMessage(rs.getString("short_message"));
                    s.setDestination(rs.getString("destination"));
                    s.setPartnerCode(rs.getString("partner_code"));
                    s.setCreatedDate(rs.getTimestamp("created_date"));
                    s.setStatus(rs.getString("status"));
                    return s;
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateStatusAndSendTime(String messageId, String status) {
        String sql = "UPDATE sms_test SET status = ?, send_time = ? WHERE message_id = ?";

        try (Connection conn = DbPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            ps.setString(3, messageId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
