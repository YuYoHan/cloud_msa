package com.example.userservice.service;

import com.example.userservice.client.OrderServiceClient;
import com.example.userservice.dto.ResponseOrder;
import com.example.userservice.dto.UserDTO;
import com.example.userservice.entity.UserEntity;
import com.example.userservice.error.FeignErrorDecoder;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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
    private final RestTemplate restTemplate;
    private final Environment env;
    private final OrderServiceClient orderServiceClient;
    private final FeignErrorDecoder feignErrorDecoder;

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
    // 실패 감지 시 getOrdersFallback 메서드 호출
    @CircuitBreaker(name = "orderService", fallbackMethod = "getOrdersFallback")
    public UserDTO getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if(userEntity == null) {
            throw new UsernameNotFoundException("user not found");
        }

        ModelMapper mapper = getModelMapper();
        UserDTO userDTO = mapper.map(userEntity, UserDTO.class);
//        String orderUrl = String.format(env.getProperty("order_service.url"), userId);
//        ResponseEntity<List<ResponseOrder>> orderListResponse =
//                restTemplate.exchange(orderUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<ResponseOrder>>() {
//        });
//        List<ResponseOrder> orderList = orderListResponse.getBody();

        // FeignClient 방법

//        List<ResponseOrder> orderList = null;
//        try {
//            orderList = orderServiceClient.getOrders(userId);
//        } catch (FeignClientException ex) {
//            log.error(ex.getMessage());
//        }

        log.debug("Before call orders microservice");
        List<ResponseOrder> orderList = orderServiceClient.getOrders(userId);
        userDTO.setOrders(orderList);
        log.debug("After called orders microservice");

        return userDTO;
    }

    // 실패시 동작하는 메서드
    // 원래 메서드의 모든 파라미터를 그대로 받아야 하고
    // 마지막 인자는 Throwable이어야 합니다
    public UserDTO getOrdersFallback(String userId, Throwable t) {
        log.warn("Fallback triggered for userId: {} due to {}", userId, t.getMessage());

        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            throw new UsernameNotFoundException("user not found");
        }

        ModelMapper mapper = getModelMapper();
        UserDTO userDTO = mapper.map(userEntity, UserDTO.class);
        userDTO.setOrders(List.of()); // 빈 주문 목록 반환

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
