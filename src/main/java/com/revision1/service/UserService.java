package com.revision1.service;

import com.revision1.payload.LoginDto;
import com.revision1.enity.User;
import com.revision1.reposistory.UserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;
    private JWTService jwtservice;
    public UserService(UserRepository userRepository, JWTService jwtservice) {
        this.userRepository = userRepository;
        this.jwtservice = jwtservice;
    }
    public String verifyLogin(LoginDto loginDto) {
        Optional<User> opUser = userRepository.findByUsername(loginDto.getUsername());
        if (opUser.isPresent()) {
            User user = opUser.get();
            if (BCrypt.checkpw(loginDto.getPassword(),user.getPassword()))
            {
                String token = jwtservice.generateToken(user.getUsername());
                return token;
            }
            else {
                return null;
            }

        }

        return null;
    }
}
