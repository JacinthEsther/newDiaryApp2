package com.example.diaryapp2.controllers;


import com.example.diaryapp2.dtos.UserDto;
import com.example.diaryapp2.exceptions.DiaryApplicationException;
import com.example.diaryapp2.models.Diary;
import com.example.diaryapp2.models.User;
import com.example.diaryapp2.responses.ApiResponse;
import com.example.diaryapp2.services.DiaryService;
import com.example.diaryapp2.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("api/v3/diaryApp/diaries")
public class DiaryController {
    private DiaryService diaryService;

    private UserService userService;

    @Autowired
    public DiaryController(DiaryService diaryService, UserService userService) {
        this.diaryService = diaryService;
        this.userService = userService;
    }

    @PostMapping("/create/{userId}")// always reference the path variable before the request params
    private ResponseEntity<?> createDiary(@Valid @NotNull @NotBlank @PathVariable("userId") String userId,
                                          @NotNull @NotBlank @RequestParam(name = "title") String title){
        try {
//            log.info("Hit endpoint");
            User user = userService.findById(Long.valueOf(userId));
            Diary diary = diaryService.createDiary(title,user);
            Diary savedDiary = userService.addDiary(Long.valueOf(userId), diary);
            ApiResponse apiResponse = ApiResponse.builder()
                    .payLoad(savedDiary)
                    .isSuccessful(true)
                    .message("diary added successfully")
                    .statusCode(201)
                    .build();
            return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);

        } catch (DiaryApplicationException e) {
            ApiResponse apiResponse = ApiResponse.builder()
                    .message(e.getMessage())
                    .isSuccessful(false)
                    .statusCode(404)
                    .build();
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException exception){
        Map<String, String> errors = new HashMap();
        exception.getBindingResult().getAllErrors().forEach(
                (error ->{
                    String fieldName= ((FieldError)error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                })
        );
        return errors;
    }
}
