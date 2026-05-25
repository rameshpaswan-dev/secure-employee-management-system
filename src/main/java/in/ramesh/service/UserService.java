package in.ramesh.service;

import in.ramesh.entity.User;
import in.ramesh.payload.LoginResponse;
import in.ramesh.payload.UserRequest;
import in.ramesh.payload.UserResponse;

public interface UserService {

    // Register new user
    UserResponse register(UserRequest request);

    // Login user
    LoginResponse login(UserRequest request);

    // Find user by username
    User findByUsername(String username);
}