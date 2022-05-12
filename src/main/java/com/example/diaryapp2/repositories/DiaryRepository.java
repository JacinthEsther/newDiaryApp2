package com.example.diaryapp2.repositories;

import com.example.diaryapp2.models.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary,Long> {
}
