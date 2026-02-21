package com.example.MeetingSummarizer.Controller;

import com.example.MeetingSummarizer.Response.UIResponse.UserResponse;
import com.example.MeetingSummarizer.Service.SummarizeGeminiApi;
import com.example.MeetingSummarizer.Service.TranscriptOpenAi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/transcribe")
@CrossOrigin(origins = "*")
public class TranscriptionController {
    private final TranscriptOpenAi transcriptService;

    private final SummarizeGeminiApi summarizeGeminiApi;

    public TranscriptionController(TranscriptOpenAi transcriptService, SummarizeGeminiApi geminiApi) {
        this.transcriptService = transcriptService;
        this.summarizeGeminiApi = geminiApi;
    }

    @PostMapping("/getSummarizedText")
    public UserResponse transcribeAudio(@RequestParam("file") MultipartFile file) throws IOException{
        ResponseEntity<String> transcribedResponse = transcriptService.getTranscript(file);

        return summarizeGeminiApi.getSummary(transcribedResponse.toString());
    }


}
