package com.example.MeetingSummarizer.Response.UIResponse;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
@Data
public class UserResponse {

    private String id;
    private String summarizedText;
    private String title;
    private List<String> actionItems;
    private LocalDateTime createdAt;

}
