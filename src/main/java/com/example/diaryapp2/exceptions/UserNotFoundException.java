package com.example.diaryapp2.exceptions;

public class UserNotFoundException extends DiaryApplicationException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
