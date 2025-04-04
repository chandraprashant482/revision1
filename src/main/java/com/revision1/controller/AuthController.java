package com.revision1.controller;


import com.revision1.payload.JwtToken;
import com.revision1.payload.LoginDto;
import com.revision1.enity.User;
import com.revision1.payload.ProfileDto;
import com.revision1.reposistory.UserRepository;
import com.revision1.service.JWTService;
import com.revision1.service.OtpService;
import com.revision1.service.SmsService;
import com.revision1.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private UserRepository userRepository;
    private UserService userService;
    private OtpService otpService;
    private JWTService jwtService;
    private SmsService smsService;
    public AuthController(UserRepository userRepository, UserService userService, OtpService otpService, JWTService jwtService, SmsService smsService) {

        this.userRepository = userRepository;
        this.userService = userService;
        this.otpService = otpService;
        this.jwtService = jwtService;
        this.smsService = smsService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> createUser(
            @RequestBody User user) {
        Optional<User> byUsername = userRepository.findByUsername(user.getUsername());
        if (byUsername.isPresent()) {
            return new ResponseEntity<>("User Name Already Exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Optional<User> byEmail = userRepository.findByEmailId(user.getEmailId());
        if (byEmail.isPresent()) {
            return new ResponseEntity<>("Email Already Exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Optional<User> byMobile = userRepository.findByMobile(user.getMobile());
        if (byMobile.isPresent()) {
            return new ResponseEntity<>("Mobile Already Exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        user.setPassword(BCrypt.hashpw(user.getPassword(),BCrypt.gensalt(5)));
        user.setRole("ROLE_USER");
        userRepository.save(user);
        return new ResponseEntity<>("User Created Successfully", HttpStatus.OK);
    }
    @PostMapping("/property/sign-up")
    public ResponseEntity<?> createPropertyOwnerAccount(
            @RequestBody User user) {
        Optional<User> byUsername = userRepository.findByUsername(user.getUsername());
        if (byUsername.isPresent()) {
            return new ResponseEntity<>("User Name Already Exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Optional<User> byEmail = userRepository.findByEmailId(user.getEmailId());
        if (byEmail.isPresent()) {
            return new ResponseEntity<>("Email Already Exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Optional<User> byMobile = userRepository.findByMobile(user.getMobile());
        if (byMobile.isPresent()) {
            return new ResponseEntity<>("Mobile Already Exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        user.setPassword(BCrypt.hashpw(user.getPassword(),BCrypt.gensalt(5)));
        user.setRole("ROLE_OWNER");
        userRepository.save(user);
        return new ResponseEntity<>("Property OWNER Created Successfully", HttpStatus.OK);
    }
    @PostMapping("/blog/sign-up")
    public ResponseEntity<?> createBlogManagerAccount(
            @RequestBody User user) {
        Optional<User> byUsername = userRepository.findByUsername(user.getUsername());
        if (byUsername.isPresent()) {
            return new ResponseEntity<>("User Name Already Exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Optional<User> byEmail = userRepository.findByEmailId(user.getEmailId());
        if (byEmail.isPresent()) {
            return new ResponseEntity<>("Email Already Exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Optional<User> byMobile = userRepository.findByMobile(user.getMobile());
        if (byMobile.isPresent()) {
            return new ResponseEntity<>("Mobile Already Exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        user.setPassword(BCrypt.hashpw(user.getPassword(),BCrypt.gensalt(5)));
        user.setRole("ROLE_BLOGMANAGER");
        userRepository.save(user);
        return new ResponseEntity<>("BLOG MANAGER Created Successfully", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> Login(
            @RequestBody LoginDto dto){
        String token = userService.verifyLogin(dto);
        JwtToken jwtToken=new JwtToken();
        jwtToken.setToken(token);
        jwtToken.setType("JWT");
        if (token != null){
            return new ResponseEntity<>(jwtToken,HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Invalid",HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/sent-otp")
    public ResponseEntity<?> loginWithOtp(@RequestParam String mobileNumber) {
        Optional<User> userOptional = userRepository.findByMobile(mobileNumber);

        if (userOptional.isEmpty()) {
            return new ResponseEntity<>("Mobile number not registered", HttpStatus.NOT_FOUND);
        }

        String otp = otpService.generateOtp(mobileNumber);
        smsService.sendSms( "+91"+mobileNumber,otp);
        return new ResponseEntity<>("OTP sent successfully " , HttpStatus.OK);
    }


    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String mobileNumber, @RequestParam String otp) {
        boolean isOtpValid = otpService.validateOtp(mobileNumber, otp);

        if (!isOtpValid) {
            return new ResponseEntity<>("Invalid or expired OTP", HttpStatus.UNAUTHORIZED);
        }

        // Fetch the user from the database
        Optional<User> userOptional = userRepository.findByMobile(mobileNumber);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        User user = userOptional.get();
        String jwtToken = jwtService.generateToken(user.getUsername());
        JwtToken jwtToke=new JwtToken();
        jwtToke.setToken(jwtToken);
        jwtToke.setType("JWT");

        return new ResponseEntity<>(jwtToke,HttpStatus.OK);
    }



    @GetMapping("/viewProfile")
    public ResponseEntity<ProfileDto> viewProfile(
            @AuthenticationPrincipal User user
    ){
        Optional<User> byUsername = userRepository.findByUsername(user.getUsername());
        User user1 = byUsername.get();
        ProfileDto profileDto =new ProfileDto();
        profileDto.setUsername(user1.getUsername());
        profileDto.setEmail(user1.getEmailId());
        profileDto.setName(user1.getName());
        profileDto.setPhoneNumber(user1.getMobile());
        return new ResponseEntity<>(profileDto,HttpStatus.OK);


    }

}
