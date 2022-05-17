package com.example.diaryapp2.controllers;


import com.example.diaryapp2.dtos.LoginRequest;
import com.example.diaryapp2.exceptions.UserNotFoundException;
import com.example.diaryapp2.models.User;
import com.example.diaryapp2.security.jwt.TokenProvider;
import com.example.diaryapp2.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v3/diaryApp/auth")
@Slf4j
public class AuthController {

    @Autowired
   private UserService userService;

    @Autowired
   private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) throws UserNotFoundException {


        Authentication authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                        loginRequest.getPassword())

        );


        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String token= tokenProvider.generateJWTToken(authentication);
        log.info("Responding2");
        User user= userService.findUserByEmail(loginRequest.getEmail());
        log.info("Responding13");
        return new ResponseEntity<>(new AuthToken(token, user.getId()), HttpStatus.OK);
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
