package com.example.diaryapp2.services;

import com.example.diaryapp2.dtos.UserDto;
import com.example.diaryapp2.exceptions.DiaryApplicationException;
import com.example.diaryapp2.models.Diary;
import com.example.diaryapp2.models.User;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
public interface UserService {

    UserDto createAccount(String email, String password) throws DiaryApplicationException;
    Diary addDiary(@NotNull Long id, @NotNull Diary diary) throws DiaryApplicationException;
    User findById( Long userId) throws DiaryApplicationException;
    boolean deleteUser(User user);
}
