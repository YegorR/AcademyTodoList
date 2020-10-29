package ru.yegorr.todolist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yegorr.todolist.dto.request.UserRequest;
import ru.yegorr.todolist.dto.response.*;
import ru.yegorr.todolist.entity.UserEntity;
import ru.yegorr.todolist.exception.*;
import ru.yegorr.todolist.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Реализация UserService
 */
@Transactional
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * Конструктор
     *
     * @param userRepository userRepository
     */
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse createUser(UserRequest userRequest) throws UniqueCheckFallsException {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new UniqueCheckFallsException("email");
        }
        if (userRepository.existsByNickname(userRequest.getNickname())) {
            throw new UniqueCheckFallsException("nickname");
        }
        UserEntity userEntity = new UserEntity();
        fillUserEntity(userEntity, userRequest);
        userEntity.setId(UUID.randomUUID());
        userEntity = userRepository.save(userEntity);
        return generateUserResponse(userEntity);
    }

    @Override
    public UserResponse changeUser(UserRequest userRequest, UUID userId) throws NotFoundException, UniqueCheckFallsException {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format("User %s", userId)));
        if (!userEntity.getEmail().equals(userRequest.getEmail()) && userRepository.existsByEmail(userRequest.getEmail())) {
            throw new UniqueCheckFallsException("email");
        }
        if (!userEntity.getNickname().equals(userRequest.getNickname()) && userRepository.existsByNickname(userRequest.getNickname())) {
            throw new UniqueCheckFallsException("nickname");
        }
        fillUserEntity(userEntity, userRequest);
        return generateUserResponse(userEntity);
    }

    @Override
    public UserResponse getUser(UUID userId) throws NotFoundException {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format("User %s", userId)));
        return generateUserResponse(userEntity);
    }

    @Override
    public UsersResponse getAllUsers() {
        List<UserResponse> users = userRepository.findAll().stream().map(UserServiceImpl::generateUserResponse).collect(Collectors.toList());
        UsersResponse usersResponse = new UsersResponse();
        usersResponse.setUsers(users);
        return usersResponse;
    }

    @Override
    public void deleteUser(UUID userId) throws NotFoundException {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User %s", userId));
        }
        userRepository.deleteById(userId);
    }

    private static UserResponse generateUserResponse(UserEntity userEntity) {
        UserResponse userResponse = new UserResponse();
        userResponse.setUserId(userEntity.getId());
        userResponse.setEmail(userEntity.getEmail());
        userResponse.setNickname(userEntity.getNickname());
        userResponse.setPhone(userEntity.getPhone());
        return userResponse;
    }

    private void fillUserEntity(UserEntity userEntity, UserRequest userRequest) throws UniqueCheckFallsException {
        userEntity.setEmail(userRequest.getEmail());
        userEntity.setNickname(userRequest.getNickname());
        userEntity.setPassword(userRequest.getPassword());
        userEntity.setPhone(userRequest.getPhone());
    }
}
