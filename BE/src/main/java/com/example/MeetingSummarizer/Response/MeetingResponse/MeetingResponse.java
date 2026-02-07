package com.example.MeetingSummarizer.Response.MeetingResponse;

import lombok.Data;
import java.util.List;

@Data
public class MeetingResponse {

    private String title;
    private String summary;
    private List<String> actionItems;
}
