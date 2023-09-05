package com.example.Blog.Hub.controller;

import com.example.Blog.Hub.dto.UserDTO;
import com.example.Blog.Hub.entity.User;
import com.example.Blog.Hub.service.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long userId){
        UserDTO userDTO = userService.getUserById(userId);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/user")
    public ResponseEntity<UserDTO> getUserByUsername(@RequestParam String username){
        User user = userService.findByEmail(username);
        return ResponseEntity.ok(modelMapper.map(user,UserDTO.class));
    }

    @PutMapping(value = "/user",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUser( @Valid @RequestPart UserDTO userDTO, @RequestPart("image") MultipartFile image){
        UserDTO updatedUser = userService.updateUser(userDTO,image);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId){
        userService.deleteUser(userId);
        return ResponseEntity.ok("Deleted Successfully");
    }
}
