package com.produce.sms.repository;

import com.produce.sms.entity.SmsTest;
import com.produce.sms.util.DbPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SmsRepository {
    public List<SmsTest> findByStatus(String status) {
        String sql = "SELECT * FROM sms_test WHERE status = ?";
        List<SmsTest> list = new ArrayList<>();
        try (Connection conn = DbPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SmsTest s = new SmsTest();
                    s.setMessageId(rs.getString("message_id"));
                    s.setKeyword(rs.getString("keyword"));
                    s.setSender(rs.getString("sender"));
                    s.setShortMessage(rs.getString("short_message"));
                    s.setDestination(rs.getString("destination"));
                    s.setPartnerCode(rs.getString("partner_code"));
                    s.setCreatedDate(rs.getTimestamp("created_date"));
                    s.setStatus(rs.getString("status"));
                    list.add(s);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public int claimSms(String messageId, String expected, String toStatus) {
        String sql = "UPDATE sms_test SET status=? WHERE message_id=? AND status=?";
        try (Connection conn = DbPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, toStatus);
            ps.setString(2, messageId);
            ps.setString(3, expected);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
