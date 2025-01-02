package org.xumin.securedoc.dtorequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRequest {
    @NotEmpty(message = "First name cannot be empty or null")
    private String firstName;
    @NotEmpty(message = "Last name cannot be empty or null")
    private String lastName;
    @NotEmpty(message = "Email cannot be empty or null")
    private String email;
    @NotEmpty(message = "password cannot be empty or null")
    private String password;
    private String bio;
    private String phone;
}
