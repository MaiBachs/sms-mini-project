package com.sms.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StatusCode {
    public static final String SUCCESS = "00";
    public static final String BAD_REQUEST = "ER40001";
    public static final String UN_AUTHENTICATED = "ER40101";
    public static final String NOT_PERMIT = "ER40301";
    public static final String NOT_FOUND = "ER40401";
    public static final String SYSTEM_ERROR = "ER50001";

}
