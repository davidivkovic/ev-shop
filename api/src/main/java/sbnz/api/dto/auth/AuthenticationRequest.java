package sbnz.api.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthenticationRequest {
    @NotBlank @Email @Size(max = 128)
    public String email;

    @NotBlank @Size(min = 6, max = 128)
    public String password;

}