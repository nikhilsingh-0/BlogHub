package com.example.Blog.Hub.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "user")
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(unique = true)
    @Email(message = "Not a valid Email")
    @NotEmpty(message = "email can't be empty")
    private String email;
    @NotEmpty(message = "password can't be empty")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String image;
    private String organisation;
    private String about;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonManagedReference
    private List<Post> posts;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Comment> comments;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Like> likes;
}
