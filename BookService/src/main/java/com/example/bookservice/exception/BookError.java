package com.example.bookservice.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@Data
@Schema(description = "Entity for sending a response in case of an error related to books")
public class BookError {

    @Schema(description = "Error message")
    private String message;

    @Schema(description = "Timestamp of the error occurrence", example = "2024-11-07 10:30:00")
    private String timestamp;

    public BookError(String message) {
        this.message = message;
        this.timestamp = formatTimestamp();
    }

    private String formatTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("en", "US"));
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        return sdf.format(new Date());
    }
}
