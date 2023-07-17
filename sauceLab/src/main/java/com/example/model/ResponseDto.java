package com.example.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Data
@Component
public class ResponseDto {

    private int resposneCode;
    private String message;
    private Object resposneObject;


}
