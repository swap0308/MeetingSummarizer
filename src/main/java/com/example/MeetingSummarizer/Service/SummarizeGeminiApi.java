package com.example.MeetingSummarizer.Service;

import com.example.MeetingSummarizer.Response.GeminiResponse.Candidate;
import com.example.MeetingSummarizer.Response.GeminiResponse.Content;
import com.example.MeetingSummarizer.Response.GeminiResponse.GemResponse;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class SummarizeGeminiApi {

    @Value("${gemini.api.url}")
    private String geminiUrl;


    @Value("${gemini.api.key}")
    private String geminiKey;


    private final WebClient webClient;

    public SummarizeGeminiApi(WebClient webClient){
        this.webClient = webClient;
    }


    private String processRequest(String request){

        StringBuilder prompt = new StringBuilder();

        prompt.append("Provide a clear and concise summary of the below meeting, also mention action items if any.\n\n");
        prompt.append(request);

        return prompt.toString();
    }

    Map<String, Object> getRequestBody(String prompt){
        return Map.of("contents", List.of(
                Map.of("parts", List.of(
                        Map.of("text", prompt)
                ))
        ));
    }

    public String extractRequiredResponseFromGeminiResponse(GemResponse gemResponse){

        try{
            if(null != gemResponse || !gemResponse.getCandidates().isEmpty()){
                Candidate candidate = gemResponse.getCandidates().getFirst();

                if(null != candidate.getContent()){
                    Content content = candidate.getContent();

                    if(null != content.getParts() && !content.getParts().isEmpty()){
                        String result = content.getParts().getFirst().getText();
                        return result;
                    }
                }
            }

            return "Some error occurred in getting the response.........";

        }catch (Exception e){
            return ("Error in getting response" + e.getMessage());
        }
    }

    public String getSummary(String request){

        String prompt = processRequest(request);
        Map<String, Object> requestBody = getRequestBody(prompt);

        GemResponse gemResponse = webClient.post()
                .uri(geminiUrl+geminiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(
                        status -> status.isError(),
                        r -> r.bodyToMono(String.class)
                                .map(err -> new RuntimeException("Gemini error: " + err))
                )
                .bodyToMono(GemResponse.class)
                .block();

        System.out.println("The response is : \n\n");
        System.out.println(gemResponse);
        System.out.print("\n\n\n\n");

        return extractRequiredResponseFromGeminiResponse(gemResponse);
    }


}
