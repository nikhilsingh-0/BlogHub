package com.example.Blog.Hub.service;

import com.example.Blog.Hub.entity.Like;
import com.example.Blog.Hub.entity.Post;
import com.example.Blog.Hub.entity.User;
import com.example.Blog.Hub.repository.LikeRepository;
import com.example.Blog.Hub.repository.PostRepository;
import com.example.Blog.Hub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeServiceImpl implements LikeService{

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public String toggleLike(Long postId) {

        Post post = postRepository.findById(postId).get();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());

        Optional<Like> existingLike = likeRepository.findByPostIdAndUserId(postId,user.getId());

        if (existingLike.isEmpty()){
            Like like = new Like();
            like.setUser(user);
            like.setPost(post);
            post.setLikeCount(post.getLikeCount()+1);
            likeRepository.save(like);
            postRepository.save(post);
            return "Liked";
        }else{
            likeRepository.delete(existingLike.get());
            post.setLikeCount(post.getLikeCount()-1);
            postRepository.save(post);
            return "Unliked";
        }
    }

    @Override
    public boolean isLiked(Long postId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());
        if(user==null){
            return false;
        }
        Optional<Like> existingLike = likeRepository.findByPostIdAndUserId(postId, user.getId());
        return existingLike.isPresent();
    }
}
