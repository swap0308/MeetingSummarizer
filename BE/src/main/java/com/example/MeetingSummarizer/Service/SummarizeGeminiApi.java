package src.main.java.com.example.MeetingSummarizer.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import src.main.java.com.example.MeetingSummarizer.BaseFunctions.BaseServices;
import src.main.java.com.example.MeetingSummarizer.Response.GeminiResponse.Candidate;
import src.main.java.com.example.MeetingSummarizer.Response.GeminiResponse.Content;
import src.main.java.com.example.MeetingSummarizer.Response.GeminiResponse.GemResponse;
import src.main.java.com.example.MeetingSummarizer.Response.UIResponse.UserResponse;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SummarizeGeminiApi {

    @Value("${gemini.api.url}")
    private String geminiUrl;


    @Value("${gemini.api.key}")
    private String geminiKey;


    private final WebClient webClient;
    private final BaseServices baseServices;


    public SummarizeGeminiApi(WebClient webClient, BaseServices baseServices){
        this.webClient = webClient;
        this.baseServices = baseServices;
    }


    private String processRequest(String request){

        StringBuilder prompt = new StringBuilder();

        prompt.append("Provide a title and clear and concise summary ,also mention action items if any.\n\n");
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

    public UserResponse extractRequiredResponseFromGeminiResponse(GemResponse gemResponse){

        UserResponse response = new UserResponse();

        try{
            if(null != gemResponse && !gemResponse.getCandidates().isEmpty() && !gemResponse.getCandidates().isEmpty()){
                Candidate candidate = gemResponse.getCandidates().getFirst();

                if(null != candidate.getContent()){
                    Content content = candidate.getContent();

                    if(null != content.getParts() && !content.getParts().isEmpty()){
                        String result = content.getParts().getFirst().getText();

                        String title = getTitleFromText(result);
                        String summary = getSummaryFromText(result);
                        String actionItems = getActionItemsFromText(result);

                        if(baseServices.isNullOrEmpty(title)){
                            response.setTitle(title);
                        }

                        if(baseServices.isNullOrEmpty(summary)){
                            response.setSummarizedText(summary);
                        }

                        if(baseServices.isNullOrEmpty(actionItems)){
                            response.setActionItems(actionItems);
                        }
                    }
                }
            }
            return response;
        }catch (Exception e){
        return response; // Handling needs to be done...
    }
}

    private String getActionItemsFromText(String result) {
        Pattern p = Pattern.compile("### \\*\\*Action Items\\*\\*([\\s\\S]*)");
        Matcher m = p.matcher(result);
        return m.find() ? m.group(1).trim() : "";
    }

    private String getSummaryFromText(String result) {
        Pattern p = Pattern.compile("### \\*\\*Summary\\*\\*([\\s\\S]*?)### \\*\\*Action Items\\*\\*");
        Matcher m = p.matcher(result);
        return m.find() ? m.group(1).trim() : "";
    }

    private String getTitleFromText(String result) {
        Pattern p = Pattern.compile("### \\*\\*Title:\\s*(.*?)\\*\\*");
        Matcher m = p.matcher(result);
        return m.find() ? m.group(1).trim() : "";
    }

    public UserResponse getSummary(String request){

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

    System.out.println(gemResponse);
    System.out.print("\n\n\n\n");

    return extractRequiredResponseFromGeminiResponse(gemResponse);
}


}
