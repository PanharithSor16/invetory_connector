package com.KIT.connector.controller;

import com.KIT.connector.dto.UserDto;
import com.KIT.connector.dto.UserRegisterDto;
import com.KIT.connector.jwt.JwtHelper;
import com.KIT.connector.model.User;
import com.KIT.connector.repository.UserRepository;
import com.KIT.connector.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
@CrossOrigin("*")
@RestController
@RequestMapping("auth/")
public class UserController {

    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("login")
    public ResponseEntity<?> login(@Valid @RequestBody UserDto userDto) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String accessToken = jwtHelper.generateToken(userDetails);
            String refreshToken = jwtHelper.generateRefreshToken(userDetails);
            Map<String, String> token = new HashMap<>();
            token.put("accessToken", accessToken);
            token.put("refreshToken", refreshToken);
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid username or password");
        }
    }

    @PostMapping("create")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterDto userRegisterDto) {
        if (userRepository.existsByUsername(userRegisterDto.getUsername())) {
            return ResponseEntity.badRequest().body("Username is already registered");
        }
        String encodedPassword = passwordEncoder.encode(userRegisterDto.getPassword());
        User newUser = new User(userRegisterDto.getUsername(), encodedPassword, userRegisterDto.getFullName());
        userRepository.save(newUser);
        UserDetails userDetails = userService.loadUserByUsername(newUser.getUsername());
        String jwt = jwtHelper.generateToken(userDetails);
        Map<String, String> response = new HashMap<>();
        response.put("token", jwt);
        return ResponseEntity.ok(response);
    }
}