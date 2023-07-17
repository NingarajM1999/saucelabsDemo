package com.example.controller;

import com.example.handler.imp.SauceLabs;
import com.example.model.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;

@RestController
@RequestMapping("/saucelabs")
public class SauceLabsController {

    @Autowired
    SauceLabs sauceLabs;
    @GetMapping("/downloadVideo")
    public ResponseDto downLoadVideo(@RequestParam String userName,@RequestParam String accessKey,@RequestParam String sessionId) throws Exception {
        return sauceLabs.downloadVideo(userName,accessKey,sessionId);
    }
    @GetMapping("/sauceLabsLogs")
    public ResponseDto sauceLabsLogs(@RequestParam String userName,@RequestParam String accessKey,@RequestParam String sessionId){
        return sauceLabs.sauceLabsLogs(userName,accessKey,sessionId);
    }

    @GetMapping("/startingTime&endTime")
    public ResponseDto startingTimeAndEndTime(@RequestParam String userName,@RequestParam String accessKey,@RequestParam String sessionId){
        return sauceLabs.startingTimeAndEndTime(userName,accessKey,sessionId);
    }


}
