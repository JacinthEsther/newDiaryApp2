package com.example.diaryapp2.controllers;


import com.example.diaryapp2.dtos.UserDto;
import com.example.diaryapp2.exceptions.DiaryApplicationException;
import com.example.diaryapp2.responses.ApiResponse;
import com.example.diaryapp2.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("api/v3/diaryApp")
public class userController {

    private UserService userService;

    public userController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users.create")
    public ResponseEntity<?> createUser(@RequestParam @Valid @NotNull @NotBlank String email,
                                        @RequestParam @Valid @NotNull @NotBlank String password){
        try{
            UserDto userDto=userService.createAccount(email, password);

            ApiResponse response = ApiResponse.builder()
                    .payLoad(userDto)
                    .isSuccessful(true)
                    .statusCode(201)
                    .message("user created successfully")
                    .build();
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }catch(DiaryApplicationException e){

            ApiResponse response = ApiResponse.builder()

                    .isSuccessful(false)
                    .statusCode(400)
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
