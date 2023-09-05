package com.example.Blog.Hub.repository;

import com.example.Blog.Hub.entity.ForgetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<ForgetPasswordToken,Long> {
    ForgetPasswordToken findByToken(String token);
    ForgetPasswordToken findByEmail(String email);
}
