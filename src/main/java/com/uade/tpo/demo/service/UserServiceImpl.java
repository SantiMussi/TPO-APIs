package com.uade.tpo.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.uade.tpo.demo.entity.User;
import com.uade.tpo.demo.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public Page<User> getUsers(PageRequest pageRequest) {
        return userRepository.findAll(pageRequest);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    @Override
    public User changeUserInfo(long UserId, String email, String name, String password, String firstName, String lastName) {
        User u = userRepository.findById(UserId).orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        //Si algun parametro es null, no se actualiza
        if (email != null) u.setEmail(email);
        if (name != null) u.setName(name);
        if (password != null) u.setPassword(password);
        if (firstName != null) u.setFirstName(firstName);
        if (lastName != null) u.setLastName(lastName);
        
        return userRepository.save(u);
    }
    


}
