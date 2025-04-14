package com.example.userservice.service;

import com.example.userservice.dto.ResponseOrder;
import com.example.userservice.dto.UserDTO;
import com.example.userservice.entity.UserEntity;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username);

        if(userEntity == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(userEntity.getEmail(), userEntity.getEncryptedPw(), true, true, true, true,
                new ArrayList<>());
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        userDTO.setUserId(UUID.randomUUID().toString());
        log.debug("userDTO {}", userDTO);
        ModelMapper mapper = getModelMapper();
        UserEntity userEntity = mapper.map(userDTO, UserEntity.class);
        log.debug("유저 엔티티 {}", userEntity);
        userEntity.setEncryptedPw(passwordEncoder.encode(userDTO.getPw()));
        UserEntity save = userRepository.save(userEntity);
        return mapper.map(save, UserDTO.class);
    }

    private static ModelMapper getModelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper;
    }

    @Override
    public UserDTO getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if(userEntity == null) {
            throw new UsernameNotFoundException("user not found");
        }

        ModelMapper mapper = getModelMapper();
        UserDTO userDTO = mapper.map(userEntity, UserDTO.class);

        List<ResponseOrder> orders = new ArrayList<>();
        userDTO.setOrders(orders);

        return userDTO;
    }

    @Override
    public Iterable<UserDTO> getUserByAll() {
        List<UserEntity> users = userRepository.findAll();
        ModelMapper modelMapper = getModelMapper();
        return users.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserDetailsByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        UserDTO userDTO = new ModelMapper().map(userEntity, UserDTO.class);
        return userDTO;
    }
}
