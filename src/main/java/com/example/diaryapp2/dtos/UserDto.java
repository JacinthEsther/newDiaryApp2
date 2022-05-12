package com.example.diaryapp2.dtos;


import com.example.diaryapp2.models.Diary;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties("diaries")
public class UserDto {

    private Long id;
    private String email;
    private Set<Diary> diaries;
}
