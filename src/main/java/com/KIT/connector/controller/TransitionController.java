package com.KIT.connector.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.KIT.connector.dto.BalanceDto;

import com.KIT.connector.dto.TransitionDto;
import com.KIT.connector.jwt.JwtHelper;
import com.KIT.connector.model.Transition;
import com.KIT.connector.repository.TransitionRepository;
import com.KIT.connector.service.UserService;
import jakarta.validation.Valid;
import jdk.jfr.TransitionTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
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

        TransitionDto lastTransition =  transitionRepository.getTransitionCompare(reqTransition.getItemCode(), reqTransition.getItemName());

       try {
           String token = authorization.replace("Bearer", "");
           String username = jwtHelper.extractUsername(token);
           UserDetails userDetails = userService.loadUserByUsername(username);
           boolean isAuth = jwtHelper.validateToken(token, userDetails);
           if (isAuth && lastTransition.getStockValue() + (reqTransition.getReceivedQty() - reqTransition.getIssuedQty()) >= 0){
               Transition transition = new Transition(
                       username,
                       reqTransition.getItemCode(),
                       reqTransition.getItemName(),
                       reqTransition.getReceivedQty(),
                       reqTransition.getIssuedQty(),
                       reqTransition.getLocation(),
                       reqTransition.getRemark(),
                       reqTransition.getTransferType()
               );
               transitionRepository.save(transition);
               return ResponseEntity.ok(HttpStatus.CREATED);
           }else if (lastTransition.getStockValue() + (reqTransition.getReceivedQty() - reqTransition.getIssuedQty()) <= 0){
               return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Stock Value can't last than 0");
           }else {
               return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
           }
       }catch (Exception e) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request: " + e.getMessage());
       }
    }
    @GetMapping("get")
    public ResponseEntity<?> getTransfer( @RequestHeader(HttpHeaders.AUTHORIZATION) String authorized,
                                          @RequestParam(required = false) String NameCode){
        try {
            String token = authorized.replace("Bearer ", "").trim();
            String username = jwtHelper.extractUsername(token);
            UserDetails userDetails = userService.loadUserByUsername(username);
            if (jwtHelper.validateToken(token, userDetails) && NameCode != null) {
                List<Transition> transitionList = transitionRepository.getAllItem(NameCode);
                return ResponseEntity.ok(transitionList);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request: " + e.getMessage());
        }
    }
    @GetMapping("balance/get")
    public ResponseEntity<?> getBalance(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorized,
                                        @RequestParam(required = false) String NameCode){
        try {
            String token = authorized.replace("Bearer ", "").trim();
            String username = jwtHelper.extractUsername(token);
            UserDetails userDetails = userService.loadUserByUsername(username);
            if (jwtHelper.validateToken(token, userDetails) &&  NameCode != null) {
                List<BalanceDto> balanceDtoList = transitionRepository.getTransitionBalance(NameCode);
                return ResponseEntity.ok(balanceDtoList);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request: " + e.getMessage());
        }
    }
    @GetMapping("getByUser")
    public ResponseEntity<?> getTransferByUser( @RequestHeader(HttpHeaders.AUTHORIZATION) String authorized,
                                          @RequestParam(required = false) String NameCode){
        try {
            String token = authorized.replace("Bearer ", "").trim();
            String username = jwtHelper.extractUsername(token);
            UserDetails userDetails = userService.loadUserByUsername(username);
            if (jwtHelper.validateToken(token, userDetails) && NameCode == null) {
                List<Transition> transitionList = transitionRepository.getAllItemUser(username);
                return ResponseEntity.ok(transitionList);
            } else if (jwtHelper.validateToken(token, userDetails) &&  NameCode != null) {
                List<Transition> transitionList = transitionRepository.getAllItemByUser(NameCode, username);
                return ResponseEntity.ok(transitionList);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request: " + e.getMessage());
        }
    }

    // I should @PutMapping but it has some problem so I use
    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorized,
                                        @PathVariable("id") long id){
        try {
            Optional<Transition> transition = transitionRepository.findById(id);
            TransitionDto toDelete = transitionRepository.getTransitionCompare(transition.get().getItemCode(), transition.get().getItemName());
            String token = authorized.replace("Bearer ", "").trim();
            String username = jwtHelper.extractUsername(token);
            UserDetails userDetails = userService.loadUserByUsername(username);
            if (jwtHelper.validateToken(token, userDetails) && (toDelete.getStockValue() - transition.get().getReceivedQty()) >= 0){
//                transitionRepository.deleteById(id);
                transition.get().setStatus(false);
                transition.get().setRegisterDate(LocalDateTime.now());
                transitionRepository.save(transition.get());
                return ResponseEntity.status(HttpStatus.ACCEPTED).body("Success");
            }else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request: " + e.getMessage());
        }
    }

}
    