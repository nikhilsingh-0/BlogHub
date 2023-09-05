package com.example.Blog.Hub.service;

import com.example.Blog.Hub.dto.UserDTO;
import com.example.Blog.Hub.entity.Post;
import com.example.Blog.Hub.entity.User;
import com.example.Blog.Hub.exception.UserNotFoundException;
import com.example.Blog.Hub.repository.UserRepository;
import com.example.Blog.Hub.utills.ImageConstants;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;


    @Override
    public UserDTO createUser(User user) {
        return modelMapper.map(repository.save(user),UserDTO.class);
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO,MultipartFile file) {
        Optional<User> optional = repository.findById(userDTO.getId());
        if (optional.isEmpty()){
            throw new UserNotFoundException("User Not Found with id "+userDTO.getId());
        }
        User user = optional.get();
        user.setName(userDTO.getName());

        if (file!=null && !file.isEmpty()){
            if (!file.getName().equals(user.getImage())){
                String filename = imageUrl(file);
                user.setImage(filename);
            }
        }

        user.setOrganisation(userDTO.getOrganisation());
        user.setAbout(userDTO.getAbout());
        return modelMapper.map(repository.save(user),UserDTO.class);

    }

    @Override
    public UserDTO getUserById(Long id) {
        Optional<User> optional = repository.findById(id);
        if (optional.isEmpty()){
            throw new UserNotFoundException("User Not Found with id "+id);
        }
        User user = optional.get();
        List<Post> postList = user.getPosts().stream().sorted(Comparator.comparing(Post::getId).reversed()).toList();
        user.setPosts(postList);
        return modelMapper.map(optional.get(),UserDTO.class);
    }

    @Override
    public void deleteUser(Long id) {
        repository.deleteById(id);
    }

    @Override
    public String updatePassword(String email,String password) {
        repository.updatePassword(email,password);
        return "Succsess";
    }

    @Override
    public User findByEmail(String username) {
        return repository.findByEmail(username);
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
