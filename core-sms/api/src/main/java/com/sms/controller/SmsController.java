package com.sms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.dto.request.SmsRequest;
import com.sms.dto.response.GeneralResponse;
import com.sms.dto.response.ReportSmsResponse;
import com.sms.dto.response.sms.SmsResponse;
import com.sms.service.SmsService;
import com.sms.service.impl.SmsServiceImpl;
import com.sms.util.Constant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SmsController implements HttpHandler {
    private static final Logger log = LogManager.getLogger(SmsController.class);
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
        InputStream is = exchange.getRequestBody();
        SmsRequest req = mapper.readValue(is, SmsRequest.class);
        log.info("=== Receive sms to api: {} ===", req);
        try {
            service.receiverSmsApi(req);
            GeneralResponse<SmsResponse> resp = new GeneralResponse<>();
            byte[] bytes = mapper.writeValueAsBytes(resp);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, bytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(bytes);
            os.close();
        } catch (RuntimeException e) {
            log.error("Error processing request", e);
            exchange.sendResponseHeaders(400, -1);
        }
    }

    public static class ReportHandler implements HttpHandler {
        private static final Logger log = LogManager.getLogger(ReportHandler.class);
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
