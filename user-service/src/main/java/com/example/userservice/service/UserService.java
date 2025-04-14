package com.example.userservice.service;

import com.example.userservice.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserDTO createUser(UserDTO userDTO);
    UserDTO getUserByUserId(String userId);
    Iterable<UserDTO> getUserByAll();

    UserDTO getUserDetailsByEmail(String email);
}
