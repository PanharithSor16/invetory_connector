package com.KIT.connector.controller;

import com.KIT.connector.jwt.JwtHelper;
import com.KIT.connector.model.MasterItem;
import com.KIT.connector.model.Transition;
import com.KIT.connector.repository.MasterItemRepository;
import com.KIT.connector.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin("*")
@Controller
@RequestMapping("auth/masteritem")
public class MasterItemController {
    @Autowired
    JwtHelper jwtHelper;
    @Autowired
    UserService userService;
    @Autowired
    MasterItemRepository masterItemRepository;
    @PostMapping("create")
    public ResponseEntity<?> createMasterItem(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorized
    ,@Valid @RequestBody MasterItem reqMasterItem){

        String token = authorized.replace("Bearer ", "").trim();
        String username = jwtHelper.extractUsername(token);
        UserDetails userDetails = userService.loadUserByUsername(username);
        boolean isAuth = jwtHelper.validateToken(token, userDetails);
        MasterItem checkIsAlready= masterItemRepository.findItemCode(reqMasterItem.getItemCode());
        if (isAuth && checkIsAlready != null){
            return ResponseEntity.status((HttpStatus.BAD_REQUEST)).body("The code is already");
        } else if (isAuth && checkIsAlready == null) {
            MasterItem masterItem = new MasterItem(
                    reqMasterItem.getItemName(),
                    reqMasterItem.getItemCode(),
                    reqMasterItem.getLocation(),
                    reqMasterItem.getMaker(),
                    reqMasterItem.getMOQ(),
                    reqMasterItem.getType()
            );
            masterItemRepository.save(masterItem);
            return ResponseEntity.ok(HttpStatus.CREATED);
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
    }
    @GetMapping("get")
    public ResponseEntity<?> getMasterItem(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorized,
                                           @RequestParam(required = false) String NameCode){
        try {
            String token = authorized.replace("Bearer ", "").trim();
            String username = jwtHelper.extractUsername(token);
            UserDetails userDetails = userService.loadUserByUsername(username);
            if (jwtHelper.validateToken(token, userDetails) && NameCode == null){
                List<MasterItem> masterItemList = masterItemRepository.findAll();
                return ResponseEntity.ok(masterItemList);
            } else if (jwtHelper.validateToken(token, userDetails) && NameCode != null) {
                List<MasterItem> masterItemList = masterItemRepository.getItemByNameCode(NameCode);
                return ResponseEntity.ok(masterItemList);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid request: " + e.getMessage());
        }
    }
    @GetMapping("getNameCode")
    public ResponseEntity<?> getItemNameCode(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorized,
                                             @RequestParam(required = false) String NameCode){
        try{
            String token = authorized.replace("Bearer ", "").trim();
            String username = jwtHelper.extractUsername(token);
            UserDetails userDetails = userService.loadUserByUsername(username);
            if (jwtHelper.validateToken(token, userDetails) && NameCode != null){
                List<MasterItem> masterItemList = masterItemRepository.getItemByNameCode(NameCode);
                return ResponseEntity.ok(masterItemList);
            } else if (jwtHelper.validateToken(token, userDetails) && NameCode == null) {
                List<MasterItem> masterItemList = masterItemRepository.findAll();
                return ResponseEntity.ok(masterItemList);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid request: " + e.getMessage());
        }
    }
    @GetMapping(value = "get/{id}")
    public ResponseEntity<?> getItemById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorized,
                                         @PathVariable Long id){
       try {
           String token = authorized.replace("Bearer ", "").trim();
           String username = jwtHelper.extractUsername(token);
           UserDetails userDetails = userService.loadUserByUsername(username);
           if (jwtHelper.validateToken(token, userDetails) ){
               MasterItem masterItem = masterItemRepository.findAllById(id);
               return ResponseEntity.ok(masterItem);
           } else {
               return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
           }
       } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid request: " + e.getMessage());
       }
    }
    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorized,
                                        @PathVariable("id") long id){
        try {
            String token = authorized.replace("Bearer ", "").trim();
            String username = jwtHelper.extractUsername(token);
            UserDetails userDetails = userService.loadUserByUsername(username);
            if (jwtHelper.validateToken(token, userDetails)){
                masterItemRepository.deleteById(id);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body("Success");
            }else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request: " + e.getMessage());
        }
    }
}
