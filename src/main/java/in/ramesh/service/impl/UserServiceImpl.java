package in.ramesh.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import in.ramesh.entity.User;
import in.ramesh.exception.ResourceNotFoundException;
import in.ramesh.payload.LoginResponse;
import in.ramesh.payload.UserRequest;
import in.ramesh.payload.UserResponse;
import in.ramesh.repository.UserRepository;
import in.ramesh.service.UserService;
import in.ramesh.util.JwtUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public UserResponse register(UserRequest request) {

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role(request.getRole())
                .build();

        User savedUser = repo.save(user);

        return UserResponse.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .message("User registered successfully")
                .build();
    }

    @Override
    public LoginResponse login(UserRequest request) {

        User user = repo.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
     // ✅ Step 1: check email properly (if you really want it)
        if (!user.getEmail().equals(request.getEmail())) {
            throw new RuntimeException("Invalid email");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        Date expiryDate = jwtUtil.extractExpiration(token);

        return LoginResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole())
                .expiresAt(convertToIST(expiryDate))
                .message("Login successful")
                .build();
    }

    @Override
    public User findByUsername(String username) {

        return repo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }

    private String convertToIST(Date date) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        return sdf.format(date);
    }
}