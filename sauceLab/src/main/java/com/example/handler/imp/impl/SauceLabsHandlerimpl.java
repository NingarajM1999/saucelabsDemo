package com.example.handler.imp.impl;

import com.example.handler.imp.SauceLabs;
import com.example.model.DataModel;
import com.example.model.ResponseDto;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Component
public class SauceLabsHandlerimpl implements SauceLabs {

    @Value("${apiEndPointForVideo}")
    private String apiEndPointForVideo;

    @Value("${apiEndPointForSaucelabs}")
    private String apiForSauceLabs;

    @Value("${apiForStartingAndEndingTime}")
    private String apiForStartingAndEndingTime;
    @Override
    public ResponseDto downloadVideo(String username, String accessKey, String sessionId) throws MalformedURLException {
        ResponseDto responseDto = new ResponseDto();
        String apiEndpoint = "https://api.eu-central-1.saucelabs.com/rest/v1/";
        String sessionUrl = apiEndPointForVideo.replace("{userName}",username).replace("{sessionId}",sessionId);

        try {

            URL url = new URL(sessionUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", getAuthHeader(username, accessKey));
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {

                InputStream inputStream = connection.getInputStream();
                String responseBody = new String(inputStream.readAllBytes());
                inputStream.close();

                String videoUrl = extractVideoUrl(responseBody);

                URL videoUrlObj = new URL(sessionUrl);
                HttpURLConnection videoConnection = (HttpURLConnection) videoUrlObj.openConnection();
                videoConnection.setRequestMethod("GET");
                videoConnection.setRequestProperty("Authorization", getAuthHeader(username, accessKey));
                int videoResponseCode = videoConnection.getResponseCode();

                if (videoResponseCode == HttpURLConnection.HTTP_OK) {
                    InputStream videoInputStream = videoConnection.getInputStream();

                    Path outputPath = Path.of("C:\\Users\\Ningaraj\\Desktop\\sauce\\download2.mp4");
                    Files.copy(videoInputStream, outputPath, StandardCopyOption.REPLACE_EXISTING);
                    videoInputStream.close();

                }

                videoConnection.disconnect();
            }

            connection.disconnect();
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        return responseDto;
    }

    @Override
    public ResponseDto sauceLabsLogs(String userName, String accessKey, String sessionId) {
        ResponseDto responseDto = new ResponseDto();
        String sessionUrl = apiForSauceLabs.replace("userName",userName).replace("sessionId",sessionId);

        try {
            URL url = new URL(sessionUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", getAuthHeader(userName, accessKey));

            int responseCode = connection.getResponseCode();
            StringBuilder response = new StringBuilder();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
            }
            responseDto.setMessage("all logs as been fetched ");
            responseDto.setResposneObject(response);
            responseDto.setResposneCode(HttpStatus.OK.value());

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseDto;
    }

    @Override
    public ResponseDto startingTimeAndEndTime(String userName, String accessKey, String sessionId) {
        ResponseDto responseDto = new ResponseDto();
        String sessionUrl = apiForStartingAndEndingTime.replace("userName",userName).replace("sessionId",sessionId);

        try {
            URL url = new URL(sessionUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", getAuthHeader(userName, accessKey));

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JsonArray jsonArray = JsonParser.parseString(response.toString()).getAsJsonArray();

                List<Map<String, Double>> logs = new ArrayList<>();
                if (Objects.nonNull(logs)) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                        double startTime = jsonObject.get("start_time").getAsDouble();
                        double duration = jsonObject.get("duration").getAsDouble();
                        double inVideoTimeline = jsonObject.get("in_video_timeline").getAsDouble();

                        Map<String, Double> log = new HashMap<>();
                        log.put("start_time", startTime);
                        log.put("duration", duration);
                        log.put("in_video_timeline", inVideoTimeline);
                        logs.add(log);
                    }
                    responseDto.setMessage("data as been found");
                    responseDto.setResposneCode(HttpStatus.OK.value());
                    responseDto.setResposneObject(logs);
                } else {
                    responseDto.setResposneCode(HttpStatus.NOT_FOUND.value());
                    responseDto.setMessage("not data in found");
                }
            } else {
                responseDto.setMessage("Failed to fetch logs. Response Code: " + responseCode);
            }

            connection.disconnect();
        } catch (IOException e) {
            responseDto.setMessage("Error occurred: " + e.getMessage());
        }

        return responseDto;
    }


    private static String getAuthHeader(String username, String accessKey) {
        String credentials = username + ":" + accessKey;
        return "Basic " + java.util.Base64.getEncoder().encodeToString(credentials.getBytes());
    }

    private static String extractVideoUrl(String responseBody) {
        return "URL_TO_VIDEO";
    }
}



