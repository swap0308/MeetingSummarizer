package com.example.MeetingSummarizer.Repository;

import com.example.MeetingSummarizer.Entity.Meeting_Insights;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MeetingInsightRepository extends JpaRepository<Meeting_Insights, Long> {

    Optional<Meeting_Insights> findByMeetingUUID(String UUId);
}
