package com.consumer.sms.util;

public class Constant {
    public static class SmsStatus {
        public static final String NONE = "NONE";
        public static final String COMPLETE = "COMPLETE";
        public static final String FAILED = "FAILED";
        public static final String PROCESSING = "PROCESSING";
    }

    public static class SmsData {
        public static final String ENCRYPT_MESSAGE_DEFAULT = "";
        public static final Integer IS_ENCRYPT_DEFAULT = 0;
        public static final Integer TYPE_DEFAULT = 0;
    }

    public static class GatewayStatus {
        public static final String STATUS_SUCCESS = "200";
    }
}
