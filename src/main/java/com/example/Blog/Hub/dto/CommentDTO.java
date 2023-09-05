package com.example.Blog.Hub.dto;

import com.example.Blog.Hub.entity.Post;
import com.example.Blog.Hub.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CommentDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "Comment can't be empty")
    @Size(min = 2,max = 200,message = "size must in between 2 and 200")
    private String comment;
    private Date date;
    private String userName;
    private String userImage;
    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonBackReference
    private Post post;
}
