package com.produce.sms.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constant {
    public static final String PHONE_REGEX = "^(84|0)[0-9]{9}$";

    @UtilityClass
    public final class SmsStatus {
        public static final String NONE = "NONE";
        public static final String COMPLETE = "COMPLETE";
        public static final String FAILED = "FAILED";
        public static final String PROCESSING = "PROCESSING";
    }
}
