package com.example.diaryapp2.services;

import com.example.diaryapp2.dtos.UserDto;
import com.example.diaryapp2.exceptions.DiaryApplicationException;
import com.example.diaryapp2.exceptions.UserNotFoundException;
import com.example.diaryapp2.models.Diary;
import com.example.diaryapp2.models.Role;
import com.example.diaryapp2.models.User;
import com.example.diaryapp2.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class UserServiceImpl implements UserService, UserDetailsService{

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
         //u can't save the user cos you need to map the user to keep the password secret
          return mapper.map(savedUser, UserDto.class);
      }
      throw new DiaryApplicationException("email already exist");
    }

    @Override
    public Diary addDiary(@NotNull Long id,@NotNull Diary diary) throws DiaryApplicationException {
        User user = userRepository.findById(id).orElseThrow(()-> new DiaryApplicationException("user does not exist"));
//        user.addDiary(diary);
        user.addDiary(diary);
         userRepository.save(user);
        return diary;
    }

    @Override
    public User findById(Long userId) throws DiaryApplicationException {
        return userRepository.findById(userId).orElseThrow(()->new DiaryApplicationException("user does not exist"));
    }

    @Override
    public boolean deleteUser(User user) {
        userRepository.delete(user);
        return true;
    }

    @Override
    public User findUserByEmail(String email) throws UserNotFoundException {
       return userRepository.findUserByEmail(email).orElseThrow(()->
                new UserNotFoundException("user name not found"));
    }

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user =userRepository.findUserByEmail(email).orElseThrow(()-> new UserNotFoundException("user not found"));
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),user.getPassword(),




                getAuthorities(user.getRoles()));

    }

    private Collection<? extends GrantedAuthority> getAuthorities(Set<Role> roles) {
        Set<SimpleGrantedAuthority> authorities = roles.stream().map(
                role-> new SimpleGrantedAuthority(role.getRoleType().name())
        ).collect(Collectors.toSet());
        return authorities;
    }
}