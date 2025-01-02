package org.xumin.securedoc.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.xumin.securedoc.domain.ApiAuthentication;
import org.xumin.securedoc.domain.UserPrincipal;
import org.xumin.securedoc.exception.ApiException;
import org.xumin.securedoc.service.UserService;

import java.util.function.Consumer;
import java.util.function.Function;

import static java.time.LocalDateTime.now;
import static org.xumin.securedoc.constant.Constants.NINETY_DAYS;
import static org.xumin.securedoc.domain.ApiAuthentication.authenticated;

@Component
@RequiredArgsConstructor
public class ApiAuthenticationProvider implements AuthenticationProvider {
    private final UserService userService;
    private final BCryptPasswordEncoder encoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
       var apiAuthentication = authenticationFunction.apply(authentication);
       var user = userService.getUserByEmail(apiAuthentication.getEmail());
       if(user != null) {
           var userCredential = userService.getUserCredentialById(user.getId());
//           if(userCredential.getUpdatedAt().minusDays(NINETY_DAYS).isAfter(now())) {
//               throw new ApiException("Credential expired. Please reset your password.");
//           }
           if(user.isAccountNonExpired()) {
               throw new ApiException("Credential expired. Please reset your password.");
           }
           var userPrincipal = new UserPrincipal(user, userCredential);
           validAccount.accept(userPrincipal);
           if(encoder.matches(apiAuthentication.getPassword(), userCredential.getPassword())) {
               return authenticated(user, userPrincipal.getAuthorities());
           } else throw new BadCredentialsException("Email and/or password incorrect. Please try again");
       } else throw new ApiException("Unable to authenticate");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ApiAuthentication.class.isAssignableFrom(authentication);
    }

    private final Function<Authentication, ApiAuthentication> authenticationFunction = authentication -> (ApiAuthentication) authentication;

    private final Consumer<UserPrincipal> validAccount = userPrincipal -> {
        if(userPrincipal.isAccountNonLocked()){
            throw new LockedException("Your account is currently locked");
        }
        if(userPrincipal.isEnabled()){
            throw new DisabledException("Your account is currently disabled");
        }
        if(userPrincipal.isCredentialsNonExpired()){
            throw new CredentialsExpiredException("Your password is expired. Please update your password.");
        }
        if(userPrincipal.isAccountNonExpired()){
            throw new DisabledException("Your account has expired. Please contact administrator.");
        }
    };
}




























