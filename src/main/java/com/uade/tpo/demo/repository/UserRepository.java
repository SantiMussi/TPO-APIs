package com.uade.tpo.demo.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.uade.tpo.demo.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String mail);

    @Query(value = "SELECT u.email, u.name, u.firstName, u.lastName, u.id, u.role FROM User u")
    Page<User> getUsers(PageRequest pageRequest);

    @Query(value = "SELECT u FROM User u WHERE u.id = ?1")
    int getUserById(long UserId);

    @Modifying
    @Query(value = "UPDATE User u SET u.email = ?2, u.name = ?3, u.password = ?4, u.firstName = ?5, u.lastName = ?6 WHERE u.id = ?1")
    int changeUserInfo(long UserId, String email, String name, String password, String firstName, String lastName);

    boolean existsByEmail(String email);
}
