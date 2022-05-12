package com.example.diaryapp2.services;


import com.example.diaryapp2.models.Diary;
import com.example.diaryapp2.models.User;
import org.springframework.stereotype.Service;

@Service
public class DiaryServiceImpl implements DiaryService{


    @Override
    public Diary createDiary(String title, User user) {
        Diary diary =new Diary(title);
        diary.setUser(user);
        return diary;
    }
}
