package com.uade.tpo.demo.controllers.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.demo.entity.User;
import com.uade.tpo.demo.exceptions.UserDuplicateException;
import com.uade.tpo.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private UserService userService;

    private final PasswordEncoder passwordEncoder;

    @PutMapping("/{id}")
    public ResponseEntity<Object> changeUserInfo(@PathVariable Long id, @RequestBody UserChangeRequest request){
        try{
            String pass = null;
            if(request.getPassword() != null){
                pass = passwordEncoder.encode(request.getPassword());
            }
            User updatedUser = userService.changeUserInfo(id, request.getEmail(), request.getName(), pass, request.getFirstName(), request.getLastName());
            return ResponseEntity.ok(updatedUser);
        } catch (UserDuplicateException e) {
            return ResponseEntity.status(409).body("Email already exists.");
        }
        
    }

}
