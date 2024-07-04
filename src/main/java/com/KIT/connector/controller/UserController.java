package com.KIT.connector.controller;

import com.KIT.connector.dto.UserDto;
import com.KIT.connector.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("auth/")
public class UserController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    UserService userService;
    @PostMapping("login")
    public Map<String, String> login(@RequestBody UserDto userDto) throws Exception{
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword())
            );
        } catch (Exception e) {
            throw new Exception("Incorrect username or password" , e);
        }
        final UserDetails userDetails = userService.loadUserByUsername(userDto.getUsername());
        return null;
    }
}
