package com.library.librarian.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

    @NotBlank(message = "Email couldn't be blank")
    @Email(message = "Invalid Email Format")
    private String email;
    private String password;

    public @NotBlank(message = "Email couldn't be blank") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "Email couldn't be blank") String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
