package com.sms.validator;

import com.sms.dto.request.SmsRequest;
import com.sms.util.Constant;
import com.sms.util.ErrorMessage;

public class SmsValidate {
    public static void validateSms(SmsRequest sms) {
        if (sms.getMessageId() == null || sms.getMessageId().isEmpty() || sms.getMessageId().length() > 32) {
            throw new RuntimeException(ErrorMessage.MESSAGE_ID_INVALID);
        }
        if (sms.getKeyword() == null || sms.getKeyword().isEmpty()) {
            throw new RuntimeException(ErrorMessage.KEYWORD_INVALID);
        }
        if (sms.getSender() == null || sms.getSender().isEmpty()) {
            throw new RuntimeException(ErrorMessage.SENDER_INVALID);
        }
        if (sms.getDestination() == null || sms.getDestination().isEmpty() || !sms.getDestination().matches(Constant.PHONE_REGEX)) {
            throw new RuntimeException(ErrorMessage.DESTINATION_INVALID);
        }
        if (sms.getShortMessage() == null || sms.getShortMessage().isEmpty()) {
            throw new RuntimeException(ErrorMessage.SHORT_MESSAGE_INVALID);
        }
    }
}
