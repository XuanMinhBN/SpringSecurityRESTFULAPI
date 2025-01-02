package org.xumin.securedoc.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xumin.securedoc.cache.CacheStore;
import org.xumin.securedoc.domain.RequestContext;
import org.xumin.securedoc.dto.User;
import org.xumin.securedoc.entity.ConfirmationEntity;
import org.xumin.securedoc.entity.CredentialEntity;
import org.xumin.securedoc.entity.RoleEntity;
import org.xumin.securedoc.entity.UserEntity;
import org.xumin.securedoc.enumeration.Authority;
import org.xumin.securedoc.enumeration.EventType;
import org.xumin.securedoc.enumeration.LoginType;
import org.xumin.securedoc.event.UserEvent;
import org.xumin.securedoc.exception.ApiException;
import org.xumin.securedoc.repository.ConfirmationRepository;
import org.xumin.securedoc.repository.CredentialRepository;
import org.xumin.securedoc.repository.RoleRepository;
import org.xumin.securedoc.repository.UserRepository;
import org.xumin.securedoc.service.UserService;
import org.xumin.securedoc.utils.UserUtils;

import java.util.Map;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static org.xumin.securedoc.utils.UserUtils.createUserEntity;
import static org.xumin.securedoc.utils.UserUtils.fromUserEntity;

@Service
@Transactional(rollbackFor=Exception.class)
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CredentialRepository credentialRepository;
    private final ConfirmationRepository confirmationRepository;
    private final CacheStore<String, Integer> userCache;
    private final ApplicationEventPublisher publisher;

    @Override
    public void createUser(String firstname, String lastname, String email, String password) {
        var user = userRepository.save(createNewUser(firstname, lastname, email));
        var credentialEntity = new CredentialEntity(user, password);
        credentialRepository.save(credentialEntity);
        var confirmationEntity = new ConfirmationEntity(user);
        confirmationRepository.save(confirmationEntity);
        publisher.publishEvent(new UserEvent(user, EventType.REGISTRATION, Map.of("key", confirmationEntity.getKey())));
    }

    @Override
    public RoleEntity getRoleName(String name) {
        var role = roleRepository.findByNameIgnoreCase(name);
        return role.orElseThrow(() -> new ApiException("Role not found"));
    }

    @Override
    public void verifyAccountKey(String key) {
        var confirmationEntity = getUserConfirmation(key);
        var userEntity = getUserEntityByEmail(confirmationEntity.getUserEntity().getEmail());
        userEntity.setEnabled(true);
        userRepository.save(userEntity);
        confirmationRepository.delete(confirmationEntity);
    }

    @Override
    public void updateLoginAttempt(String email, LoginType loginType) {
        var userEntity = getUserEntityByEmail(email);
        RequestContext.setUserId(userEntity.getId());
        switch (loginType) {
            case LOGIN_ATTEMPT -> {
                if(userCache.get(userEntity.getEmail()) == null){
                    userEntity.setLoginAttempts(0);
                    userEntity.setAccountNonLocked(true);
                }
                userEntity.setLoginAttempts(userEntity.getLoginAttempts() + 1);
                userCache.put(userEntity.getEmail(), userEntity.getLoginAttempts());
                if(userCache.get(userEntity.getEmail()) > 5){
                    userEntity.setAccountNonLocked(false);
                }
            }
            case LOGIN_SUCCESS -> {
                userEntity.setAccountNonLocked(true);
                userEntity.setLoginAttempts(0);
                userEntity.setLastLogin(now());
                userCache.evict(userEntity.getEmail());
            }
        }
        userRepository.save(userEntity);
    }

    @Override
    public User getUserByUserId(String userId) {
        var userEntity = userRepository.findUserByUserId(userId).orElseThrow(() -> new ApiException("User not found"));
        return fromUserEntity(userEntity, userEntity.getRole(), getUserCredentialById(userEntity.getId()));
    }

    @Override
    public User getUserByEmail(String email) {
        UserEntity userEntity = getUserEntityByEmail(email);
        return fromUserEntity(userEntity, userEntity.getRole(), getUserCredentialById(userEntity.getId()));
    }

    @Override
    public CredentialEntity getUserCredentialById(Long userId) {
        var credentialsById = credentialRepository.getCredentialsByUserEntityId(userId);
        return credentialsById.orElseThrow(() -> new ApiException("Unable to find user credential"));
    }

    private UserEntity getUserEntityByEmail(String email) {
        var userByEmail = userRepository.findByEmailIgnoreCase(email);
        return userByEmail.orElseThrow(() -> new ApiException("User not found"));
    }

    private ConfirmationEntity getUserConfirmation(String key) {
        return confirmationRepository.findByKey(key).orElseThrow(() -> new ApiException("Confirmation key not found"));
    }

    private UserEntity createNewUser(String firstname, String lastname, String email) {
        var role = getRoleName(Authority.USER.name());
        return createUserEntity(firstname, lastname, email, role);
    }
}
