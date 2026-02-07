package com.example.MeetingSummarizer.Request.GeminiRequest;

import lombok.Data;

@Data
public class GenerationConfig {
    private String responseMimeType;
    private ResponseSchema responseSchema;
}
