package com.example.handler.imp;

import com.example.model.ResponseDto;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.net.MalformedURLException;

@Component
public interface SauceLabs {

    ResponseDto downloadVideo(String userName, String accessKey,String sessionId) throws MalformedURLException;

    ResponseDto sauceLabsLogs(String userName, String accessKey, String sessionId);

    ResponseDto startingTimeAndEndTime(String userName, String accessKey, String sessionId);
}
