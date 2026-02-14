package com.example.MeetingSummarizer.Request.GeminiRequest;

import com.example.MeetingSummarizer.Response.GeminiResponse.Content;
import lombok.Data;

import java.util.List;

@Data
public class GeminiRequest {
    private List<Content> contents;
    private GenerationConfig generationConfig;
}
