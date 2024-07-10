package com.KIT.connector.controller;

import com.KIT.connector.jwt.JwtHelper;
import com.KIT.connector.model.MasterItem;
import com.KIT.connector.model.Transition;
import com.KIT.connector.repository.MasterItemRepository;
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
        if (isAuth){
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
    public ResponseEntity<?> getMasterItem(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorized){
        try {
            String token = authorized.replace("Bearer ", "").trim();
            String username = jwtHelper.extractUsername(token);
            UserDetails userDetails = userService.loadUserByUsername(username);
            if (jwtHelper.validateToken(token, userDetails)){
                List<MasterItem> masterItemList = masterItemRepository.findAll();
                return ResponseEntity.ok(masterItemList);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid request: " + e.getMessage());
        }
    }
    @DeleteMapping("{id}")
    public ResponseEntity<?> DeleteMasterItem(){
        return null;
    }
}
