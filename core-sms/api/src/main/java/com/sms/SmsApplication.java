package com.sms;

import com.sms.controller.SmsController;
import com.sms.util.DbPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;

public class SmsApplication {
    private static final Logger log = LogManager.getLogger(SmsApplication.class);
    public static void main(String[] args) throws IOException {
        log.info("Starting API server on port 8021");
        DbPool.initialize();
        HttpServer server = HttpServer.create(new InetSocketAddress(8021), 0);
        server.createContext("/api/v1/sms", new SmsController());
        server.createContext("/api/v1/sms/report", new SmsController.ReportHandler());
        server.setExecutor(null);
        server.start();
        log.info("Server started");
    }
}
