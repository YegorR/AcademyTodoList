package ru.yegorr.todolist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yegorr.todolist.dto.request.*;
import ru.yegorr.todolist.dto.response.*;
import ru.yegorr.todolist.entity.*;
import ru.yegorr.todolist.exception.*;
import ru.yegorr.todolist.repository.*;

import java.util.*;
import java.util.stream.Collectors;

import static ru.yegorr.todolist.service.UserSecurityInformation.*;

/**
 * Реализация UserService
 */
@Transactional
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    /**
     * Конструктор
     *
     * @param userRepository userRepository
     * @param roleRepository roleRepository
     */
    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserResponse createUser(UserRequest userRequest) throws ApplicationException {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new UniqueCheckFallsException("email");
        }
        if (userRepository.existsByNickname(userRequest.getNickname())) {
            throw new UniqueCheckFallsException("nickname");
        }
        UserEntity userEntity = new UserEntity();
        fillUserEntity(userEntity, userRequest);
        userEntity.setId(UUID.randomUUID());

        Role role = userRequest.getRole();
        if (role == null) {
            role = Role.ROLE_USER;
        }
        userEntity.setRoles(getRoleEntities(role));
        userEntity = userRepository.save(userEntity);
        return generateUserResponse(userEntity);
    }

    @Override
    public UserResponse changeUser(UserRequest userRequest, UUID userId) throws ApplicationException {
        if (!(isAdmin() || getUserId().equals(userId))) {
            throw new ForbiddenException();
        }

        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format("User %s", userId)));
        if (!userEntity.getEmail().equals(userRequest.getEmail()) && userRepository.existsByEmail(userRequest.getEmail())) {
            throw new UniqueCheckFallsException("email");
        }
        if (!userEntity.getNickname().equals(userRequest.getNickname()) && userRepository.existsByNickname(userRequest.getNickname())) {
            throw new UniqueCheckFallsException("nickname");
        }
        fillUserEntity(userEntity, userRequest);

        Role role = userRequest.getRole();
        if (role != null) {
            userEntity.setRoles(getRoleEntities(role));
        }

        return generateUserResponse(userEntity);
    }

    @Override
    public UserResponse getUser(UUID userId) throws ApplicationException {
        if (!(isAdmin() || getUserId().equals(userId))) {
            throw new ForbiddenException();
        }

        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format("User %s", userId)));
        return generateUserResponse(userEntity);
    }

    @Override
    public UsersResponse getAllUsers() throws ApplicationException {
        if (!isAdmin()) {
            throw new ForbiddenException();
        }
        List<UserResponse> users = userRepository.findAll().stream().map(UserServiceImpl::generateUserResponse).collect(Collectors.toList());
        UsersResponse usersResponse = new UsersResponse();
        usersResponse.setUsers(users);
        return usersResponse;
    }

    @Override
    public void deleteUser(UUID userId) throws ApplicationException {
        if (!(isAdmin() || getUserId().equals(userId))) {
            throw new ForbiddenException();
        }

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

        List<Role> roles = userEntity.getRoles().stream().map(RoleEntity::getRole).collect(Collectors.toList());
        if (roles.contains(Role.ROLE_ADMIN)) {
            userResponse.setRole(Role.ROLE_ADMIN);
        } else if (roles.contains(Role.ROLE_USER)) {
            userResponse.setRole(Role.ROLE_USER);
        }
        return userResponse;
    }

    private static void fillUserEntity(UserEntity userEntity, UserRequest userRequest) {
        userEntity.setEmail(userRequest.getEmail());
        userEntity.setNickname(userRequest.getNickname());
        userEntity.setPassword(userRequest.getPassword());
        userEntity.setPhone(userRequest.getPhone());
    }

    private List<RoleEntity> getRoleEntities(Role role) throws ApplicationException {
        if (role == Role.ROLE_ADMIN && !isAdmin()) {
            throw new ForbiddenException();
        }
        List<RoleEntity> roles = new ArrayList<>();
        RoleEntity roleEntity = roleRepository.getByRole(Role.ROLE_USER).orElseThrow(() -> new ApplicationException("No role in db"));
        roles.add(roleEntity);
        if (role == Role.ROLE_ADMIN) {
            RoleEntity adminEntity = roleRepository.getByRole(Role.ROLE_ADMIN).orElseThrow(() -> new ApplicationException("No role in db"));
            roles.add(adminEntity);
        }
        return roles;
    }
}
