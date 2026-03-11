package com.sms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.dto.request.SmsRequest;
import com.sms.dto.response.GeneralResponse;
import com.sms.dto.response.ReportSmsResponse;
import com.sms.service.SmsService;
import com.sms.service.impl.SmsServiceImpl;
import com.sms.util.Constant;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.OutputStream;

public class SmsController implements HttpHandler {
    private static final Logger log = LoggerFactory.getLogger(SmsController.class);
    private final SmsService service = new SmsServiceImpl();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        if (Constant.SmsApiPath.SMS.equals(path)
                && Constant.SmsApiMethod.POST.equals(method)) {
            receiverSmsApi(exchange);
        } else {
            exchange.sendResponseHeaders(404, -1);
        }
    }

    private void receiverSmsApi(HttpExchange exchange) throws IOException {
        SmsRequest req = mapper.readValue(exchange.getRequestBody(), SmsRequest.class);
        log.info("=== Receive sms to api: {} ===", req);
        byte[] bytes;
        int status;
        try {
            service.receiverSmsApi(req);
            GeneralResponse<SmsRequest> resp = new GeneralResponse<>();
            resp.setData(req);
            bytes = mapper.writeValueAsBytes(resp);
            status = 200;
        } catch (RuntimeException e) {
            log.error("Error processing request", e);
            GeneralResponse<String> resp = new GeneralResponse<>();
            resp.setData(e.getMessage());
            bytes = mapper.writeValueAsBytes(resp);
            status = 400;
        }
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    public static class ReportHandler implements HttpHandler {
        private static final Logger log = LoggerFactory.getLogger(ReportHandler.class);
        private final SmsService service = new SmsServiceImpl();
        private final ObjectMapper mapper = new ObjectMapper();

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();
            if (Constant.SmsApiPath.SMS_REPORT.equals(path)
                    && Constant.SmsApiMethod.GET.equalsIgnoreCase(method)) {
                try {
                    GeneralResponse<ReportSmsResponse> resp = new GeneralResponse();
                    resp.setData(service.reportSms());
                    byte[] bytes = mapper.writeValueAsBytes(resp);
                    exchange.getResponseHeaders().add("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, bytes.length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(bytes);
                    os.close();
                } catch (Exception e) {
                    log.error("Error reporting", e);
                    exchange.sendResponseHeaders(500, -1);
                }
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }
}
