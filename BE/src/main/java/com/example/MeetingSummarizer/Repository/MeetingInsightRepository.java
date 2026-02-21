package com.example.MeetingSummarizer.Repository;

import com.example.MeetingSummarizer.Entity.Meeting_Insights;
import com.example.MeetingSummarizer.Response.SubMeetingResponse.AllMeetingResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface MeetingInsightRepository extends JpaRepository<Meeting_Insights, Long> {

    Optional<Meeting_Insights> findByMeetingUUID(String UUId);

    @Query("SELECT new com.example.MeetingSummarizer.Response.SubMeetingResponse.AllMeetingResponse(m.id, m.title, m.createdAt, m.meetingUUID) FROM Meeting_Insights m WHERE m.id> :cursor ORDER BY m.id ASC")
    List<AllMeetingResponse>findNext(@Param("cursor") int cursor, Pageable pageable);

}
