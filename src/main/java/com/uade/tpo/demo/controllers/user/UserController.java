package com.uade.tpo.demo.controllers.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import com.uade.tpo.demo.entity.User;
import com.uade.tpo.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.uade.tpo.demo.controllers.user.UserChangeRequest;


@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserService userService;

    @PutMapping("/{id}")
    public ResponseEntity<User> changeUserInfo(@PathVariable Long id, @RequestBody UserChangeRequest request){
        User updatedUser = userService.changeUserInfo(id, request.getEmail(), request.getName(), request.getPassword(), request.getFirstName(), request.getLastName());
        return ResponseEntity.ok(updatedUser);
    }

}
