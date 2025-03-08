package com.library.librarian.request;


import com.library.utils.annotation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LibrarianRegisterRequest {

    @NotBlank(message = "Name is Required")
    @Size(min = 4,max = 50,message = "Name must be between 4 and 50 characters")
    private String firstName;

    @NotBlank(message = "last name couldn't be blank")
    private String lastName;

    @NotBlank(message = "Email Required")
    @Email(message = "Invalid Email!")
    private String email;

    @NotBlank(message = "Password couldn't be blank")
    @ValidPassword
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;


    public @NotBlank(message = "first name couldn't be blank") String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotBlank(message = "first name couldn't be blank") String firstName) {
        this.firstName = firstName;
    }

    public @NotBlank(message = "last name couldn't be blank") String getLastName() {
        return lastName;
    }

    public void setLastName(@NotBlank(message = "last name couldn't be blank") String lastName) {
        this.lastName = lastName;
    }

    public @NotBlank(message = "Email couldn't be blank") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "Email couldn't be blank") String email) {
        this.email = email;
    }

    public @NotBlank(message = "Password couldn't be blank") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Password couldn't be blank") String password) {
        this.password = password;
    }


}
