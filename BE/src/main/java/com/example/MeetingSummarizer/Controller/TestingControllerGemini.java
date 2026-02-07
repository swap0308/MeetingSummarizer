package com.example.MeetingSummarizer.Controller;

import com.example.MeetingSummarizer.Response.UIResponse.UserResponse;
import com.example.MeetingSummarizer.Service.SummarizeGeminiApi;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transcribe")
public class TestingControllerGemini {

    private final SummarizeGeminiApi summarizeGeminiApi;

    public TestingControllerGemini(SummarizeGeminiApi summarizeGeminiApi) {
        this.summarizeGeminiApi = summarizeGeminiApi;
    }
    @PostMapping("/getGeminiResponse")
    public UserResponse getGeminiResponse(@RequestBody String input){
        System.out.println("Inside TestingControllerGemini");
        return summarizeGeminiApi.getSummary(input);
    }
}
