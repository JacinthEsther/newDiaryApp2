package com.example.diaryapp2.security;

import com.example.diaryapp2.exceptions.UnauthorizedEntryPoint;
import com.example.diaryapp2.security.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.Filter;

@Configuration  // this makes this class a bean
@EnableWebSecurity
public class applicationSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    UnauthorizedEntryPoint unauthorizedEntryPoint;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).
                passwordEncoder(bCryptPasswordEncoder());
//        super.configure(auth);
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeHttpRequests(authorize->{
                    try {
                        authorize.antMatchers("/**/users/create/**",
                                        "/**/**/**/auth/login")
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                                .and()
                                .exceptionHandling().authenticationEntryPoint(
                                        unauthorizedEntryPoint
                                )
                                .and()
                                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

                        http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
                        http.addFilterBefore(exceptionHandlerFilterBean(), JwtAuthenticationFilter.class);

                    }catch(Exception e){
                        throw new RuntimeException(e.getMessage());
                    }
                });
//                .authorizeRequests()
//                .antMatchers("**/**/**/users/create","**/**/**/users/login")
//                .permitAll()
//                .anyRequest()
//                .authenticated()
    }

    @Bean
    public Filter exceptionHandlerFilterBean() {
        return new ExceptionHandlerFilter();
    }

    @Bean
    public Filter authenticationTokenFilterBean() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
