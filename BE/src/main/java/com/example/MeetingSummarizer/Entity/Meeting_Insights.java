package com.example.MeetingSummarizer.Entity;

import com.example.MeetingSummarizer.Helper.StringListConverter;
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

    @Convert(converter = StringListConverter.class)
    @Lob
    private List<String> actionItems;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

}

