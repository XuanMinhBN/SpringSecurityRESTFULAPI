package org.xumin.securedoc.constant;

public interface Constants {
    int STRENGTH = 12;
    int NINETY_DAYS = 90;
    String LOGIN_PATH = "/user/login";
    String AUTHORITIES = "authorities";
    String GET_ARRAYS_LLC = "GET_ARRAYS_LLC";
    String EMPTY_VALUE = "empty";
    String ROLE = "role";
    String ROLE_PREFIX = "ROLE_";
    String AUTHORITY_DELIMITER = ",";
    String USER_AUTHORITIES = "document:create, document:read, document:update, document:delete";
    String ADMIN_AUTHORITIES = "user:create, user:read, user:update, document:create, document:read, document:update, document:delete";
    String SUPER_ADMIN_AUTHORITIES = "user:create, user:read, user:update, user:delete, document:create, document:read, document:update, document:delete";
    String MANAGER_AUTHORITIES = "document:create, document:read, document:update, document:delete";
}
