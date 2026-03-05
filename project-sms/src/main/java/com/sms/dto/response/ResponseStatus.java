package com.sms.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseStatus implements Serializable {
    String code;

    String message;

    @JsonFormat(pattern = "dd/MM/yyyy hh:mm a", timezone = "Asia/Ho_Chi_Minh")
    Date responseTime;

    String displayMessage;
}
