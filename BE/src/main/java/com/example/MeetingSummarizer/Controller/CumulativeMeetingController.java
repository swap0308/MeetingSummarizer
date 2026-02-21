package com.example.MeetingSummarizer.Controller;

import com.example.MeetingSummarizer.Repository.MeetingInsightRepository;
import com.example.MeetingSummarizer.Response.SubMeetingResponse.AllMeetingResponse;
import com.example.MeetingSummarizer.Response.SubMeetingResponse.PagenatedCursorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/api/meetings")
public class CumulativeMeetingController {

    @Autowired
    private MeetingInsightRepository meetingInsightRepository;

    @GetMapping("/getMeetings")
    public PagenatedCursorResponse getPaginatedMeetingResponse(@RequestParam (required = false) int cursor,
                                                               @RequestParam (defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(0, size);
        System.out.println("Cursor: " + cursor + ", Size: " + size);
        List<AllMeetingResponse> allMeeting = meetingInsightRepository.findNext(cursor, pageable);

        PagenatedCursorResponse cursorResponse = new PagenatedCursorResponse();

        if(allMeeting!=null && !allMeeting.isEmpty()){
            cursorResponse.setMeetings(allMeeting);
            cursorResponse.setCursor(allMeeting.getLast().getId());
            cursorResponse.setLastPage(allMeeting.size() < size);
        }
        return cursorResponse;
    }

}
