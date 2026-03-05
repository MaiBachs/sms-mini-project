package com.sms.validator;

import ch.qos.logback.core.util.StringUtil;
import com.sms.dto.request.SmsRequest;
import com.sms.exception.BaseResponseException;
import com.sms.util.Constant;
import com.sms.util.ErrorCode;
import com.sms.util.ErrorMessage;

public class SmsValidate {
    public static void validateSms (SmsRequest smsRequest) {
        // validate messageId
        if (StringUtil.isNullOrEmpty(smsRequest.getMessageId())
                || smsRequest.getMessageId().length() > 32) {
            throw new BaseResponseException(ErrorCode.BAD_REQUEST, ErrorMessage.MESSAGE_ID_INVALID);
        }

        // validate keyword
        if (StringUtil.isNullOrEmpty(smsRequest.getKeyword())) {
            throw new BaseResponseException(ErrorCode.BAD_REQUEST, ErrorMessage.KEYWORD_INVALID);
        }

        // validate sender
        if (StringUtil.isNullOrEmpty(smsRequest.getSender())) {
            throw new BaseResponseException(ErrorCode.BAD_REQUEST, ErrorMessage.SENDER_INVALID);
        }

        // validate sender
        if (StringUtil.isNullOrEmpty(smsRequest.getDestination())
                || !smsRequest.getDestination().matches(Constant.PHONE_REGEX)) {
            throw new BaseResponseException(ErrorCode.BAD_REQUEST, ErrorMessage.DESTINATION_INVALID);
        }
    }
}
