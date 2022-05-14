package com.example.diaryapp2.responses;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder

public class ApiResponse {

    private Object payLoad;
    private boolean isSuccessful;
    private int statusCode;
    private String message;

}
