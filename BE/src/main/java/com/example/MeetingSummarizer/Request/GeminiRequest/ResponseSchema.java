package com.example.MeetingSummarizer.Request.GeminiRequest;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class ResponseSchema {
    private String type;
    Map<String, Property> properties;
    private List<String> required;
}
