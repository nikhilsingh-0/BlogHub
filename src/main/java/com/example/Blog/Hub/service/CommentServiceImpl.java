package com.example.Blog.Hub.service;

import com.example.Blog.Hub.dto.CommentDTO;
import com.example.Blog.Hub.entity.Comment;
import com.example.Blog.Hub.entity.Post;
import com.example.Blog.Hub.entity.User;
import com.example.Blog.Hub.exception.UserNotFoundException;
import com.example.Blog.Hub.repository.CommentRepository;
import com.example.Blog.Hub.repository.PostRepository;
import com.example.Blog.Hub.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService{

    @Autowired
    CommentRepository repository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;


    @Override
    public CommentDTO createComment(CommentDTO commentDTO,Long postId) {

        Post post = postRepository.findById(postId).get();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());

        Comment comment = new Comment();
        comment.setComment(commentDTO.getComment());
        comment.setDate(new Date());
        comment.setPost(post);
        comment.setUser(user);
        Comment comment1 =  repository.save(comment);
        return modelMapper.map(comment1,CommentDTO.class);
    }

    @Override
    public CommentDTO getCommentById(Long id) {
        Optional<Comment> optional = repository.findById(id);
        if (optional.isEmpty()){
            throw new UserNotFoundException("");
        }
        Comment comment = optional.get();
        CommentDTO commentDTO = modelMapper.map(comment,CommentDTO.class);
        Optional<User> user = userRepository.findById(comment.getUser().getId());
        if (user.isPresent()){
            commentDTO.setUserImage(user.get().getImage());
            commentDTO.setUserName(user.get().getName());
        }

        return commentDTO;

    }

    @Override
    public CommentDTO updateComment(CommentDTO commentDTO) {

        Optional<Comment> optional = repository.findById(commentDTO.getId());
        if (optional.isEmpty()){
            throw new UserNotFoundException("");
        }
        Comment comment = optional.get();

        comment.setComment(commentDTO.getComment());
        comment.setDate(new Date());

        return modelMapper.map(repository.save(comment),CommentDTO.class);
    }

    @Override
    public String deleteCommentById(Long id) {
        Optional<Comment> optional = repository.findById(id);
        if (optional.isEmpty()){
            throw new UserNotFoundException("");
        }
        repository.delete(optional.get());
        return "success";
    }

    @Override
    public List<CommentDTO> getCommentByPostId(Long id) {
        List<Comment> comments = repository.findByPostId(id);
        List<CommentDTO> commentDTOS = comments.stream()
                .map(comment -> {
                    CommentDTO commentDTO = modelMapper.map(comment,CommentDTO.class);
                    Optional<User> optional = userRepository.findById(comment.getUser().getId());
                    if (optional.isPresent()){
                        commentDTO.setUserName(optional.get().getName());
                        commentDTO.setUserImage(optional.get().getImage());
                    }
                    return commentDTO;
                }).sorted(Comparator.comparing(CommentDTO::getId).reversed()).toList();

        return commentDTOS;
    }
}
