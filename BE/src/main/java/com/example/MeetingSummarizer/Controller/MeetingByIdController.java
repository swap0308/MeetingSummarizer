package com.example.MeetingSummarizer.Controller;

import com.example.MeetingSummarizer.Response.UIResponse.UserResponse;
import com.example.MeetingSummarizer.Service.MeetingInsightService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeetingByIdController {

    private final MeetingInsightService service;

    public MeetingByIdController(MeetingInsightService service) {
        this.service = service;
    }

    @GetMapping("/getMeetingByUUID/{meetingUUID}")
    public UserResponse getMeetingByID(@PathVariable String meetingUUID){

        System.out.println("Calling the controller for getMeeting via UUID");
        return service.getMeetingByUUID(meetingUUID);
    }

}
