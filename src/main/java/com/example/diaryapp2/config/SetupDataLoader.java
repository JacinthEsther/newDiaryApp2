package com.example.diaryapp2.config;

import com.example.diaryapp2.models.Role;
import com.example.diaryapp2.models.RoleType;
import com.example.diaryapp2.models.User;
import com.example.diaryapp2.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(userRepository.findUserByEmail("admin@gmail.com").isEmpty()){
            User user = new User("admin@gmail.com",passwordEncoder.encode("password123"));
            user.addRole(new Role(RoleType.ROLE_ADMIN));
            userRepository.save(user);
        }
    }
}
