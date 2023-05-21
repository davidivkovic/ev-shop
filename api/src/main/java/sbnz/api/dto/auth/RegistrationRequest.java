package sbnz.api.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegistrationRequest {
    @NotBlank @Email @Size(max = 128)
    public String email;

    @NotBlank @Size(max = 128)
    public String password;

    @NotBlank @Size(max = 64)
    public String firstName;

    @NotBlank @Size(max = 64)
    public String lastName;

    @NotBlank @Size(max = 64)
    public String role;

}
