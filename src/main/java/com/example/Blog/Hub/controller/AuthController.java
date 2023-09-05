package com.example.Blog.Hub.controller;

import com.example.Blog.Hub.dto.ForgetPasswordDTO;
import com.example.Blog.Hub.dto.LoginDTO;
import com.example.Blog.Hub.dto.ResetPasswordDTO;
import com.example.Blog.Hub.dto.UserDTO;
import com.example.Blog.Hub.entity.ForgetPasswordToken;
import com.example.Blog.Hub.entity.User;
import com.example.Blog.Hub.exception.UserNotFoundException;
import com.example.Blog.Hub.repository.TokenRepository;
import com.example.Blog.Hub.service.JWTTokenGenerationService;
import com.example.Blog.Hub.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService service;

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    JavaMailSender javaMailSender;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO){
        try{
            Authentication authentication  = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(),loginDTO.getPassword()));
            if (authentication==null || !authentication.isAuthenticated()){
                return new ResponseEntity<>("Invalid username or password",HttpStatus.BAD_REQUEST);
            }

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDTO userDTO = modelMapper.map(service.findByEmail(loginDTO.getEmail()),UserDTO.class);
            String jwt = JWTTokenGenerationService.generateJWTToken(userDTO);


            return ResponseEntity.ok(jwt);
        }catch (BadCredentialsException exception){
            return new ResponseEntity<>("Invalid username or password",HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/register")
    public ResponseEntity<String> createUser(@Valid @RequestBody User user){
        UserDTO userDTO = service.createUser(user);
        String jwt = JWTTokenGenerationService.generateJWTToken(userDTO);
        return new ResponseEntity<>(jwt,HttpStatus.CREATED);
    }

    @PostMapping("/forgetPassword")
    public ResponseEntity<String> forgetPassword(@RequestBody ForgetPasswordDTO forgetPasswordDTO) throws MessagingException {
        User user = service.findByEmail(forgetPasswordDTO.getEmail());
        if (user==null){
            throw new UserNotFoundException("user Not found");
        }

        ForgetPasswordToken tokenAvailable = tokenRepository.findByEmail(forgetPasswordDTO.getEmail());
        if (tokenAvailable!=null){
            tokenRepository.delete(tokenAvailable);
        }

        String token = UUID.randomUUID().toString();
        ForgetPasswordToken passwordToken = new ForgetPasswordToken();
        passwordToken.setToken(token);
        passwordToken.setEmail(forgetPasswordDTO.getEmail());
        passwordToken.setExpiryTime(System.currentTimeMillis()+10*60*1000);

        tokenRepository.save(passwordToken);

        String link = "http://localhost:8080/resetPassword?token=" + token;
        String result = sendMail(forgetPasswordDTO.getEmail(),link);

        return ResponseEntity.ok().body(result);

    }

    @GetMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestParam("token") String token){
        ForgetPasswordToken token1 = tokenRepository.findByToken(token);
        if (token1==null){
            return ResponseEntity.badRequest().body("Invalid Token");
        }
        if (token1.getExpiryTime()<System.currentTimeMillis()){
            tokenRepository.delete(token1);
            return ResponseEntity.badRequest().body("Invalid Token");

        }

        if (service.findByEmail(token1.getEmail())==null){
            tokenRepository.delete(token1);
            return ResponseEntity.badRequest().body("Invalid Token");
        }

        return ResponseEntity.badRequest().body("Token Valid");
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestParam("token") String token,@RequestBody ResetPasswordDTO resetPasswordDTO){
        ForgetPasswordToken token1 = tokenRepository.findByToken(token);
        if (token1==null){
            return ResponseEntity.badRequest().body("Invalid Token");
        }
        if (token1.getExpiryTime()<System.currentTimeMillis()){
            return ResponseEntity.badRequest().body("Invalid Token");
        }

        if (service.findByEmail(token1.getEmail())==null){
            return ResponseEntity.badRequest().body("Invalid Token");
        }

//        User user = service.findByEmail(token1.getEmail());
//        user.setPassword(resetPasswordDTO.getPassword());
//        service.updateUser(modelMapper.map(user,UserDTO.class));
        service.updatePassword(token1.getEmail(),resetPasswordDTO.getPassword());

        tokenRepository.delete(token1);

        return ResponseEntity.badRequest().body("success");
    }

    private String sendMail(String to,String link) throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);

        messageHelper.setTo("singhnikhilkumar81@gmail.com");
        messageHelper.setSubject("forget Password");
        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";
        messageHelper.setText(content,true);
        javaMailSender.send(mimeMessage);
        return "Email Send Successfully";
    }
}
