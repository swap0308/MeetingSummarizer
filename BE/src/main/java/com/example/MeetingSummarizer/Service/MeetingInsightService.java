package com.example.MeetingSummarizer.Service;

import com.example.MeetingSummarizer.Entity.Meeting_Insights;
import com.example.MeetingSummarizer.Repository.MeetingInsightRepository;
import com.example.MeetingSummarizer.Response.UIResponse.UserResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MeetingInsightService {

    private final MeetingInsightRepository repository;

    public MeetingInsightService(MeetingInsightRepository repository){
        this.repository = repository;
    }

    public String saveToRepository(String title, String summary, List<String> actionItems)
    {
        Meeting_Insights meetingInsights = new Meeting_Insights();

        String uuid = UUID.randomUUID().toString();

        meetingInsights.setSummary(summary);
        meetingInsights.setTitle(title);
        meetingInsights.setActionItems(actionItems);
        meetingInsights.setMeetingUUID(uuid);

        repository.save(meetingInsights);

        return uuid;
    }

    public UserResponse getMeetingByUUID(String UUID){

        System.out.println("Inside getMeetingByUUID function");

        Optional<Meeting_Insights> optional = repository.findByMeetingUUID(UUID);

        if(optional.isEmpty()){
            throw new RuntimeException("Error founding meeting with the id");
        }

        Meeting_Insights meetingInsights = optional.get();

        UserResponse userResponse = new UserResponse();
        userResponse.setSummarizedText(meetingInsights.getSummary());
        userResponse.setTitle(meetingInsights.getTitle());
        userResponse.setActionItems(meetingInsights.getActionItems());


        System.out.println("Returning the UserResponse");
        return userResponse;
    }

}
