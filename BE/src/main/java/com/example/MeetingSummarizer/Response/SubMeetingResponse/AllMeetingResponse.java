package com.example.MeetingSummarizer.Response.SubMeetingResponse;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AllMeetingResponse {

    private Long id;
    private String uuid;
    private String title;
    private LocalDateTime createdAt;

    public AllMeetingResponse(Long id, String title, LocalDateTime createdAt, String uuid) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.uuid = uuid;
    }
}
