package com.example.Blog.Hub.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {
    @NotEmpty(message = "email can't be empty")
    private String email;
    @NotEmpty(message = "password can't be empty")
    private String password;
}
