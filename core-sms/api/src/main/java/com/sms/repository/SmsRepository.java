package com.sms.repository;

import com.sms.entity.SmsTest;
import com.sms.util.DbPool;

import java.sql.*;
import java.util.Date;

public class SmsRepository {
    public boolean existsByMessageId(String messageId) {
        String sql = "SELECT count(1) FROM sms_test WHERE messageid=?";
        try (Connection conn = DbPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, messageId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public void save(SmsTest sms) {
        String sql = "INSERT INTO sms_test(messageid,keyword,sender,short_message,destination,partnercode,createddate,status) VALUES(?,?,?,?,?,?,?,?)";
        try (Connection conn = DbPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sms.getMessageId());
            ps.setString(2, sms.getKeyword());
            ps.setString(3, sms.getSender());
            ps.setString(4, sms.getShortMessage());
            ps.setString(5, sms.getDestination());
            ps.setString(6, sms.getPartnerCode());
            ps.setTimestamp(7, new Timestamp(sms.getCreatedDate().getTime()));
            ps.setString(8, sms.getStatus());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public long countByStatus(String status) {
        String sql = "SELECT count(1) FROM sms_test WHERE status=?";
        try (Connection conn = DbPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public long count() {
        String sql = "SELECT count(1) FROM sms_test";
        try (Connection conn = DbPool.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
}
