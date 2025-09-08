package com.uade.tpo.demo.controllers.user;

import com.uade.tpo.demo.entity.User;

import lombok.Data;

@Data
public class UserChangeRequest{
    private String email;
    private String name;
    private String password;
    private String firstName;
    private String lastName;
}