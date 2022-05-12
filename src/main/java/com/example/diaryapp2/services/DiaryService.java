package com.example.diaryapp2.services;

import com.example.diaryapp2.models.Diary;
import com.example.diaryapp2.models.User;

public interface DiaryService {
    Diary createDiary(String title, User user);
}
