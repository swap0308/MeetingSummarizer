package com.example.MeetingSummarizer.Service;

import com.example.MeetingSummarizer.BaseFunctions.BaseServices;
import com.example.MeetingSummarizer.Request.GeminiRequest.GeminiRequest;
import com.example.MeetingSummarizer.Request.GeminiRequest.GenerationConfig;
import com.example.MeetingSummarizer.Request.GeminiRequest.Property;
import com.example.MeetingSummarizer.Request.GeminiRequest.ResponseSchema;
import com.example.MeetingSummarizer.Response.GeminiResponse.Candidate;
import com.example.MeetingSummarizer.Response.GeminiResponse.Content;
import com.example.MeetingSummarizer.Response.GeminiResponse.GemResponse;
import com.example.MeetingSummarizer.Response.GeminiResponse.Part;
import com.example.MeetingSummarizer.Response.MeetingResponse.MeetingResponse;
import com.example.MeetingSummarizer.Response.UIResponse.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SummarizeGeminiApi {

    @Value("${gemini.api.url}")
    private String geminiUrl;


    @Value("${gemini.api.key}")
    private String geminiKey;


    private final WebClient webClient;
    private final MeetingInsightService meetingInsightService;

    public SummarizeGeminiApi(WebClient.Builder webClient, MeetingInsightService meetingInsightService){
        this.webClient = webClient.build();
        this.meetingInsightService = meetingInsightService;
    }


    private String processRequest(String request){

        StringBuilder prompt = new StringBuilder();

        prompt.append("Provide a title and clear and concise summary ,also mention action items if any. Return the action items strictly as a JSON array of strings also mentions on whom the action items are on.\n\n\n");
        prompt.append(request);

        return prompt.toString();
    }

    GeminiRequest getRequestBody(String prompt){
        GeminiRequest geminiRequest = new GeminiRequest();

        Property stringProperty = new Property();;
        stringProperty.setType("string");

        Property arrayItem = new Property();
        arrayItem.setType("string");

        Property arrayProperty = new Property();
        arrayProperty.setType("array");
        arrayProperty.setItems(arrayItem);

        Map<String, Property> props = new HashMap<>();
        props.put("title" , stringProperty);
        props.put("summary" , stringProperty);
        props.put("actionItems" , arrayProperty);


        ResponseSchema responseSchema = new ResponseSchema();
        responseSchema.setType("object");
        responseSchema.setProperties(props);
        responseSchema.setRequired(List.of("title","summary","actionItems"));

        GenerationConfig generationConfig = new GenerationConfig();
        generationConfig.setResponseMimeType("application/json");
        generationConfig.setResponseSchema(responseSchema);

        Part part = new Part();
        part.setText(prompt);

        Content content = new Content();
        content.setParts(List.of(part));

        geminiRequest.setContents(List.of(content));
        geminiRequest.setGenerationConfig(generationConfig);

        return geminiRequest;
    }

    public void extractRequiredResponseFromGeminiResponse(UserResponse response,GemResponse gemResponse){

        System.out.println("Inside the extractRequiredResponseFromGeminiResponse function");

        try{
            if(null != gemResponse && !gemResponse.getCandidates().isEmpty() && !gemResponse.getCandidates().isEmpty()){
                Candidate candidate = gemResponse.getCandidates().getFirst();

                if(null != candidate.getContent()){
                    Content content = candidate.getContent();

                    if(null != content.getParts() && !content.getParts().isEmpty()){
                        String result = content.getParts().getFirst().getText();

                        System.out.println("GEminiResult" + result);

                        ObjectMapper objectMapper = new ObjectMapper();
                        MeetingResponse meetingResponse = objectMapper.readValue(result, MeetingResponse.class);

                        System.out.println("MeetingResponse from Gemini API " + meetingResponse);

                        String title = meetingResponse.getTitle();
                        String summary = meetingResponse.getSummary();

                        try {
                            ObjectMapper objectMapper1 = new ObjectMapper();
                            String actionItemsJson = objectMapper1.writeValueAsString(meetingResponse.getActionItems());
                            response.setActionItems(List.of(actionItemsJson));
                            System.out.println("The Action items as JSON: " + actionItemsJson);
                        } catch (Exception e) {
                            System.out.println("Error converting action items to JSON: " + e.getMessage());
                            e.printStackTrace();
                        }

                        response.setTitle(title);
                        response.setSummarizedText(summary);
                        response.setActionItems(meetingResponse.getActionItems());

                        System.out.println("The Title is " +  title);
                        System.out.println("The Summary is " +  summary);
                        System.out.println("The Action items are : " + meetingResponse.getActionItems());
                    }
                }
            }

            System.out.println("This is the userResponse :: " + response);
        }catch (Exception e){
        System.out.println("Error in getting the response" + e.getMessage());// Exception handling needs to be done.........
            e.printStackTrace();
    }
}


    public UserResponse getSummary(String request){

    String prompt = processRequest(request);

    System.out.println("Input is " + prompt);
    GeminiRequest requestBody = getRequestBody(prompt);

    System.out.println("The flow entered inside the Gemini Api request structure");

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

    System.out.println("This is the geminiResponse" + gemResponse.getCandidates().getFirst().getContent().getParts().getFirst());
    System.out.print("\n\n\n\n");

    UserResponse response = new UserResponse();
    extractRequiredResponseFromGeminiResponse(response,gemResponse);

    return getResponseAndUUIDAndSaveToRepository(response);
}

    private UserResponse getResponseAndUUIDAndSaveToRepository(UserResponse response) {
        String uuid = meetingInsightService.saveToRepository(response.getTitle(), response.getSummarizedText(), response.getActionItems());

        UserResponse saved = meetingInsightService.getMeetingByUUID(uuid);
        response.setId(uuid);
        response.setCreatedAt(saved.getCreatedAt());
        return response;
    }

}
