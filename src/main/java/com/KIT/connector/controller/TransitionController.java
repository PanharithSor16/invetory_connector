package com.KIT.connector.controller;

import com.KIT.connector.jwt.JwtHelper;
import com.KIT.connector.model.Transition;
import com.KIT.connector.repository.TransitionRepository;
import com.KIT.connector.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("auth/transfer/")
public class TransitionController {
    @Autowired
    TransitionRepository transitionRepository;
    @Autowired
    JwtHelper jwtHelper;
    @Autowired
    UserService userService;
    @PostMapping("create")
    public ResponseEntity<?> createTransfer(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization
    , @Valid @RequestBody Transition reqTransition){
        String token = authorization.replace("Bearer", "");
        String username = jwtHelper.extractUsername(token);
        UserDetails userDetails = userService.loadUserByUsername(username);
        boolean isAuth = jwtHelper.validateToken(token, userDetails);
        if (isAuth){
            Transition transition = new Transition(
                    username,
                    reqTransition.getItemCode(),
                    reqTransition.getItemName(),
                    reqTransition.getReceivedQty(),
                    reqTransition.getIssuedQty(),
                    reqTransition.getLocation()
            );
            transitionRepository.save(transition);
            return ResponseEntity.ok(HttpStatus.CREATED);
        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
    }

    @GetMapping("get")
    public ResponseEntity<?> getTransfer( @RequestHeader(HttpHeaders.AUTHORIZATION) String authorized){
        try {
            String token = authorized.replace("Bearer ", "").trim();
            String username = jwtHelper.extractUsername(token);
            UserDetails userDetails = userService.loadUserByUsername(username);

            if (jwtHelper.validateToken(token, userDetails)) {
                List<Transition> transitionList = transitionRepository.findAll();
                return ResponseEntity.ok(transitionList);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request: " + e.getMessage());
        }

    }



}
