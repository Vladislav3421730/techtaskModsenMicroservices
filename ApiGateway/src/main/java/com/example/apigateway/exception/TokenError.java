package com.example.apigateway.exception;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@Data
public class TokenError {
    private String message;

    private String timestamp;

    public TokenError(String message){
        this.message = message;
        this.timestamp = formatTimestamp();
    }

    private String formatTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        return sdf.format(new Date());
    }
}
