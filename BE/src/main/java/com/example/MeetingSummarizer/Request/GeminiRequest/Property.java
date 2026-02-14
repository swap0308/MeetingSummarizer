package com.example.MeetingSummarizer.Request.GeminiRequest;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import lombok.Data;

@Data
public class Property {
    private String type;
    private Property items;
}
