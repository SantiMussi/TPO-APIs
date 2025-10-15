package com.uade.tpo.demo.controllers.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.uade.tpo.demo.entity.User;
import com.uade.tpo.demo.exceptions.UserDuplicateException;
import com.uade.tpo.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;


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

    @GetMapping
    public ResponseEntity<List<User>> getUsers(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        if (page == null || size == null)
            return ResponseEntity.ok((userService.getUsers(PageRequest.of(0, Integer.MAX_VALUE))).getContent());

        return ResponseEntity.ok(userService.getUsers(PageRequest.of(page, size)).getContent());
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getUserMe(
            @AuthenticationPrincipal com.uade.tpo.demo.entity.User user
    ) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String role = (user.getRole() != null) ? user.getRole().name() : null;

        Map<String, Object> dto = Map.of(
                "id", user.getId(),
                "email", user.getEmail(),
                "role", role
        );

        return ResponseEntity.ok(dto);
    }

}
