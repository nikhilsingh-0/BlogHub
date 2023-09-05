package com.example.Blog.Hub.dto;

import com.example.Blog.Hub.entity.Category;
import com.example.Blog.Hub.entity.Comment;
import com.example.Blog.Hub.entity.User;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PostDTO {
    private Long id;
    @NotEmpty
    private String tittle;
    @NotEmpty
    private String content;
    private String image;
    private String count;
    private Date date;

    private Long userId;
    private String categoryTittle;
//    @JsonBackReference
//    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
//    private User user;
//    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
//    @JsonBackReference
//    private Category category;
    @JsonManagedReference
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<CommentDTO> comments;
}
