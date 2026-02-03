package src.main.java.com.example.MeetingSummarizer.Service;

import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class TranscriptOpenAi {

    private final OpenAiAudioTranscriptionModel model;

    public TranscriptOpenAi(OpenAiAudioTranscriptionModel model) {
        this.model = model;
    }

    public ResponseEntity<String> getTranscript(MultipartFile file) throws IOException {

        File tempFile = File.createTempFile("audio",".wav");
        file.transferTo(tempFile);

        OpenAiAudioTranscriptionOptions options =
                OpenAiAudioTranscriptionOptions.builder()
                        .language("en")
                        .responseFormat(OpenAiAudioApi.TranscriptResponseFormat.TEXT)
                        .temperature(0f)
                        .build();

        AudioTranscriptionPrompt request = new AudioTranscriptionPrompt(new FileSystemResource(tempFile), options);
        AudioTranscriptionResponse response = model.call(request);

        tempFile.delete();
        return new ResponseEntity<>(response.getResult().getOutput(), HttpStatus.OK);
    }
}
