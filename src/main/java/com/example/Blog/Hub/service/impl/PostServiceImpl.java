package com.example.Blog.Hub.service.impl;

import com.example.Blog.Hub.dto.CommentDTO;
import com.example.Blog.Hub.dto.PostDTO;
import com.example.Blog.Hub.dto.PostResponse;
import com.example.Blog.Hub.entity.Post;
import com.example.Blog.Hub.event.PostCreatedEvent;
import com.example.Blog.Hub.event.PostDeletedEvent;
import com.example.Blog.Hub.event.PostUpdatedEvent;
import com.example.Blog.Hub.exception.PostNotFoundException;
import com.example.Blog.Hub.repository.CategoryRepository;
import com.example.Blog.Hub.repository.PostRepository;
import com.example.Blog.Hub.repository.UserRepository;
import com.example.Blog.Hub.service.FileService;
import com.example.Blog.Hub.service.PostService;
import com.example.Blog.Hub.utills.ImageConstants;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final FileService fileService;
    private final ApplicationEventPublisher eventPublisher;


    @Transactional
    @Override
    public PostDTO createPost(PostDTO postDTO, Long userId, Long categoryId){

        Post post = modelMapper.map(postDTO, Post.class);
        post.setDate(new Date());
        post.setCount(0);
        post.setCategory(categoryRepository.findById(categoryId).get());
        post.setUser(userRepository.findById(userId).get());
//        if (image==null){
//            post.setImage("images.png");
//        }else {
//            String imageUrl = imageUrl(image);
//            post.setImage(imageUrl);
//        }
        post.setImage("images.png");
        Post savedPost = postRepository.save(post);

        eventPublisher.publishEvent(new PostCreatedEvent(savedPost));

        return modelMapper.map(savedPost,PostDTO.class);
    }

    @Override
    public PostDTO getPostById(Long id) {
        Optional<Post> optional = postRepository.findById(id);
        if (optional.isEmpty()){
            throw new PostNotFoundException("post not exist");
        }

        Post post = optional.get();
        PostDTO postDTO = modelMapper.map(post,PostDTO.class);
        postDTO.setComments(postDTO.getComments().stream().sorted(Comparator.comparing(CommentDTO::getId).reversed()).toList());
        post.setCount(post.getCount()+1);
        postRepository.save(post);
        return postDTO;
    }

    @Override
    public PostResponse getAllPost(int pageNumber, int pageSize,String sort,Long categoryId) {
        Pageable pageable = PageRequest.of(pageNumber,pageSize, Sort.by(sort).descending());
        PostResponse postResponse = new PostResponse();
        if (categoryId==-1){
            Page<Post> page = postRepository.findAll(pageable);
            List<Post> postList = page.getContent();
            List<PostDTO> postDTOList = postList.stream().map(post -> modelMapper.map(post, PostDTO.class)).toList();
            postResponse.setContent(postDTOList);
            postResponse.setPageNumber(page.getNumber());
            postResponse.setPageSize(page.getSize());
            postResponse.setTotalElement(page.getNumberOfElements());
            postResponse.setTotalPages(page.getTotalPages());
            postResponse.setLastPages(page.isLast());
        }else {
            Page<Post> page = postRepository.findPostByCategoryId(categoryId,pageable);
            List<Post> postList = page.getContent();
            List<PostDTO> postDTOList = postList.stream().map(post -> modelMapper.map(post, PostDTO.class)).toList();
            postResponse.setContent(postDTOList);
            postResponse.setPageNumber(page.getNumber());
            postResponse.setPageSize(page.getSize());
            postResponse.setTotalElement(page.getNumberOfElements());
            postResponse.setTotalPages(page.getTotalPages());
            postResponse.setLastPages(page.isLast());
        }
        return postResponse;
    }

    @Transactional
    @Override
    public PostDTO updatePost(PostDTO postDTO,MultipartFile image){
        Optional<Post> optional = postRepository.findById(postDTO.getId());
        if (optional.isEmpty()){
            throw new PostNotFoundException("post not exist");
        }

        Post post = optional.get();
        post.setTittle(postDTO.getTittle());
        post.setContent(postDTO.getContent());
        if (!post.getImage().equals(postDTO.getImage())){
            if (image==null){
                post.setImage("image.png");
            }else{
                String imageUrl = imageUrl(image);
                post.setImage(imageUrl);
            }
        }
        post.setDate(new Date());
        Post updatedPost = postRepository.save(post);
        eventPublisher.publishEvent(new PostUpdatedEvent(updatedPost));

        return modelMapper.map(updatedPost,PostDTO.class);
    }

    @Transactional
    @Override
    public String deletePostById(Long id) {
        Optional<Post> optional = postRepository.findById(id);
        if (optional.isEmpty()){
            throw new PostNotFoundException("post not exist");
        }
        postRepository.deleteById(id);
        Optional<Post> afterDeleted = postRepository.findById(id);
        if (afterDeleted.isEmpty()){
            eventPublisher.publishEvent(new PostDeletedEvent(id));
            return "Post Deleted Successfully";
        }
        return "failed to delete Post";
    }

    @Override
    public List<PostDTO> getPostsByUserId(Long userId) {
        List<Post> postList = postRepository.findPostByUserId(userId);
        return postList.stream().map(post -> modelMapper.map(post,PostDTO.class)).toList();
    }

    @Override
    public PostResponse searchPost(String query,int pageNumber,int pageSize,String sort) {

        Pageable pageable = PageRequest.of(pageNumber,pageSize,Sort.by(sort));
        Page<Post> page = postRepository.findPostByTittleContaining(query,pageable);
        List<Post> posts = page.getContent();
        List<PostDTO> postList = posts.stream().map(post->modelMapper.map(post,PostDTO.class)).toList();

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(postList);
        postResponse.setPageNumber(page.getNumber());
        postResponse.setPageSize(page.getSize());
        postResponse.setTotalElement(page.getNumberOfElements());
        postResponse.setTotalPages(page.getTotalPages());
        postResponse.setLastPages(page.isLast());
        return postResponse;
    }

    public String imageUrl(MultipartFile file) {
        if (file==null){
            return "image.png";
        }
        String filename="";
        try {
            filename = fileService.upload(ImageConstants.PATH,file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ImageConstants.BASE_URL+filename;
    }
}
