package com.example.MeetingSummarizer.BaseFunctions;

import org.springframework.stereotype.Component;

@Component
public class BaseServices {

    public boolean isNullOrEmpty(String str){
     return (null == str || str.isEmpty());
    }
}
