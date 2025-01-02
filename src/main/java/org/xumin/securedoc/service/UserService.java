package org.xumin.securedoc.service;

import org.xumin.securedoc.dto.User;
import org.xumin.securedoc.entity.CredentialEntity;
import org.xumin.securedoc.entity.RoleEntity;
import org.xumin.securedoc.enumeration.LoginType;

public interface UserService {
    void createUser(String firstname, String lastname, String email, String password);
    RoleEntity getRoleName(String name);
    void verifyAccountKey(String key);
    void updateLoginAttempt(String email, LoginType loginType);
    User getUserByUserId(String userId);
    User getUserByEmail(String email);
    CredentialEntity getUserCredentialById(Long id);
}
