package com.example.Blog.Hub.repository;

import com.example.Blog.Hub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findByEmail(String email);
    @Query("update User u set u.password=:password where u.email = :email")
    void updatePassword(String email,String password);
}
