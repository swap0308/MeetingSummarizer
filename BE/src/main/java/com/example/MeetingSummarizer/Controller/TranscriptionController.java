package com.example.MeetingSummarizer.Controller;

import com.example.MeetingSummarizer.Service.SummarizeGeminiApi;
import com.example.MeetingSummarizer.Service.TranscriptOpenAi;
import org.apache.coyote.Response;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<String> transcribeAudio(@RequestParam("file") MultipartFile file) throws IOException{
        ResponseEntity<String> transcribedResponse = transcriptService.getTranscript(file);

        String summarizedResponse = summarizeGeminiApi.getSummary(transcribedResponse.toString());

        return ResponseEntity.ok(summarizedResponse);
    }
}
