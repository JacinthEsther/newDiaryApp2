package com.example.diaryapp2.repositories;

import com.example.diaryapp2.models.Entry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntryRepository extends JpaRepository<Entry,Long> {
}
