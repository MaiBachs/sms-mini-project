package com.sms.repository;

import com.sms.dto.response.SmsTestReport;
import com.sms.entity.SmsTest;
import com.sms.util.DbPool;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SmsRepository {
    public boolean existsByMessageId(String messageId) {
        String sql = "SELECT count(1) FROM sms_test WHERE message_id=?";
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
        String sql = "INSERT INTO sms_test(message_id,keyword,sender,short_message,destination,partner_code,created_date,status) VALUES(?,?,?,?,?,?,?,?)";
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

    /**
     * Retrieve every SMS row in the table. Used for exporting the report.
     */
    public List<SmsTestReport> findAll() {
        String sql = "SELECT message_id,keyword,sender,short_message,destination,partner_code,created_date,send_time,status,description FROM sms_test";
        List<SmsTestReport> list = new ArrayList<>();
        try (Connection conn = DbPool.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                SmsTestReport s = new SmsTestReport();
                s.setMessageId(rs.getString("message_id"));
                s.setKeyword(rs.getString("keyword"));
                s.setSender(rs.getString("sender"));
                s.setShortMessage(rs.getString("short_message"));
                s.setDestination(rs.getString("destination"));
                s.setPartnerCode(rs.getString("partner_code"));
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                Timestamp ts = rs.getTimestamp("created_date");
                if (ts != null) {
                    String createdDate = ts.toLocalDateTime().format(formatter);
                    s.setCreatedDate(createdDate);
                }

                ts = rs.getTimestamp("send_time");
                if (ts != null) {
                    String sendTime = ts.toLocalDateTime().format(formatter);
                    s.setSendTime(sendTime);
                }
                s.setStatus(rs.getString("status"));
                s.setDescription(rs.getString("description"));
                list.add(s);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
