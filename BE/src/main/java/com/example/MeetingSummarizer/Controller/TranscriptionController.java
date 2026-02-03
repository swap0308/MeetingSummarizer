package src.main.java.com.example.MeetingSummarizer.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import src.main.java.com.example.MeetingSummarizer.Response.UIResponse.UserResponse;
import src.main.java.com.example.MeetingSummarizer.Service.SummarizeGeminiApi;
import src.main.java.com.example.MeetingSummarizer.Service.TranscriptOpenAi;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/transcribe")
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
