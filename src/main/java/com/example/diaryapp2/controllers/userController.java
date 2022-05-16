package com.example.diaryapp2.controllers;


import com.example.diaryapp2.dtos.UserDto;
import com.example.diaryapp2.exceptions.DiaryApplicationException;
import com.example.diaryapp2.responses.ApiResponse;
import com.example.diaryapp2.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Slf4j
@RestController
@RequestMapping("/api/v3/diaryApp")
public class userController {

    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public userController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @GetMapping("/login")
    public String login(){
        return "welcome";
    }

    @PostMapping("/users/create")
    public ResponseEntity<?> createUser(@RequestParam @Valid @NotNull @NotBlank String email,
                                        @RequestParam @Valid @NotNull String password) throws DiaryApplicationException {

            log.info("Hello");
            password = bCryptPasswordEncoder.encode(password);
            UserDto userDto=userService.createAccount(email, password);

            ApiResponse response = ApiResponse.builder()
                    .payLoad(userDto)
                    .isSuccessful(true)
                    .statusCode(201)
                    .message("user created successfully")
                    .build();
//            log.info("response",response.getMessage());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
