package com.example.libraryservice.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@Data
@Schema(description = "Entity for sending error responses")
public class LibraryError {
    @Schema(description = "Error message")
    private String message;

    @Schema(description = "Error occurrence time", example = "2024-11-07 10:30:00")
    private String timestamp;

    public LibraryError(String message){
        this.message = message;
        this.timestamp = formatTimestamp();
    }

    private String formatTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        return sdf.format(new Date());
    }
}
