package com.example.MeetingSummarizer.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "meeting_insights")
@Data
public class Meeting_Insights {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "meeting_uuid", nullable = false, unique = true)
    private String meetingUUID;

    @Lob
    private String title;

    @Lob
    private String summary;

    @Lob
    private List<String> actionItems;

    private LocalDateTime createdAt = LocalDateTime.now();

}

