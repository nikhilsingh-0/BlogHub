package com.example.Blog.Hub.service;

import com.example.Blog.Hub.dto.UserDTO;
import com.example.Blog.Hub.entity.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    UserDTO createUser(User user);
    UserDTO updateUser(UserDTO userDTO,MultipartFile file);
    UserDTO getUserById(Long userId);
    void deleteUser(Long userId);
    String updatePassword(String email,String password);
    User findByEmail(String username);
}
