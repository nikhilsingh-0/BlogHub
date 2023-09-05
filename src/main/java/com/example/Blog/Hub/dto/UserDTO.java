package com.example.Blog.Hub.dto;

import com.example.Blog.Hub.entity.Comment;
import com.example.Blog.Hub.entity.Post;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class UserDTO {

    private Long id;
    @NotEmpty(message = "Name can not be empty")
    private String name;
    @Email(message = "Not a valid Email")
    private String email;
    private String image;
    private String organisation;
    private String about;
    @JsonManagedReference
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Post> posts;
}
