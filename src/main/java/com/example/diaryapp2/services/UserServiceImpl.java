package com.example.diaryapp2.services;

import com.example.diaryapp2.dtos.UserDto;
import com.example.diaryapp2.exceptions.DiaryApplicationException;
import com.example.diaryapp2.models.Diary;
import com.example.diaryapp2.models.User;
import com.example.diaryapp2.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;
    private ModelMapper mapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.mapper = new ModelMapper();
    }

    @Override
    public UserDto createAccount(String email, String password) throws DiaryApplicationException {
      Optional<User> optionalUser=  userRepository.findUserByEmail(email);
      if(optionalUser.isEmpty()){
          User user = new User(email, password);
         User savedUser= userRepository.save(user);
         //u can't save the user cs you need to map the user to keep the password secret
          return mapper.map(savedUser, UserDto.class);
      }
      throw new DiaryApplicationException("email already exist");
    }

    @Override
    public UserDto addDiary(Long id, Diary diary) throws DiaryApplicationException {
        User user = userRepository.findById(id).orElseThrow(()->new DiaryApplicationException("user does not exist"));
//        user.addDiary(diary);
        user.getDiaries().add(diary);
        User savedUser = userRepository.save(user);
        return  mapper.map(savedUser, UserDto.class);
    }

    @Override
    public User findById(Long userId) throws DiaryApplicationException {
        return userRepository.findById(userId).orElseThrow(()->new DiaryApplicationException("user does not exist"));
    }
}
