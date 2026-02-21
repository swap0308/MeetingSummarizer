package com.example.MeetingSummarizer.Response.SubMeetingResponse;
import lombok.Data;

import java.util.List;

@Data
public class PagenatedCursorResponse {

    private long cursor;
    private List<AllMeetingResponse> meetings;
    private boolean isLastPage;
}
