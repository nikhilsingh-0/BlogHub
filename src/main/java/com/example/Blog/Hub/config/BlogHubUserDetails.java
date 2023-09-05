package com.example.Blog.Hub.config;

import com.example.Blog.Hub.entity.User;
import com.example.Blog.Hub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BlogHubUserDetails implements UserDetailsService {

    @Autowired
    UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        User user = repository.findByEmail(username);
        if (user==null){
            throw new UsernameNotFoundException("user"+username);
        }


        return new org.springframework.security.core.userdetails.User(username,user.getPassword(),authorities);
    }
}
