package com.sms.util;

public class Constant {
    public static final String PHONE_REGEX = "^(84|0)[0-9]{9}$";

    public static class SmsStatus {
        public static final String NONE = "NONE";
        public static final String PROCESSING = "PROCESSING";
        public static final String COMPLETE = "COMPLETE";
        public static final String FAILED = "FAILED";
    }

    public static class SmsApiPath {
        public static final String SMS = "/api/v1/sms";
        public static final String SMS_REPORT = "/api/v1/sms/report";
    }

    public static class SmsApiMethod {
        public static final String GET = "GET";
        public static final String POST = "POST";
    }
}
