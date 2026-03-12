package com.sms;

import com.sms.controller.SmsController;
import com.sms.util.DbPool;
import com.sms.util.Constant;

import java.io.IOException;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmsApplication {
    private static final Logger log = LoggerFactory.getLogger(SmsApplication.class);
    public static void main(String[] args) throws IOException {
        log.info("Starting API server on port 8021");
        DbPool.initialize();
        HttpServer server = HttpServer.create(new InetSocketAddress(8021), 0);
        server.createContext("/api/v1/sms", new SmsController());
        server.createContext(Constant.SmsApiPath.SMS_REPORT, new SmsController.ReportHandler());
        server.createContext(Constant.SmsApiPath.SMS_REPORT_EXPORT, new SmsController.ReportHandler());
        server.setExecutor(null);
        server.start();
        log.info("Server started");
    }
}
