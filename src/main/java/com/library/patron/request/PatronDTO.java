package com.library.patron.request;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

@Getter
@Setter
public class PatronDTO {

    @NotBlank(message = "First name is required")
    @Size(max = 50, min = 3 ,message = "First name must be at most 50 and at least 3 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must be at most 50 characters characters")
    private String lastName;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "\\d{10,16}", message = "Phone number must be between 10-15 digits")
    private String phoneNumber;

    @NotBlank(message = "Address is required")
    @Size(max = 255, message = "Address must be at most 255 characters")
    private String address;

    public PatronDTO(String firstName, String lastName, String phoneNumber, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public PatronDTO() {
    }


    public @NotBlank(message = "First name is required") @Size(max = 50, message = "First name must be at most 50 characters") String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotBlank(message = "First name is required") @Size(max = 50, message = "First name must be at most 50 characters") String firstName) {
        this.firstName = firstName;
    }

    public @NotBlank(message = "Last name is required") @Size(max = 50, message = "Last name must be at most 50 characters") String getLastName() {
        return lastName;
    }

    public void setLastName(@NotBlank(message = "Last name is required") @Size(max = 50, message = "Last name must be at most 50 characters") String lastName) {
        this.lastName = lastName;
    }

    public @NotBlank(message = "Phone number is required") @Pattern(regexp = "\\d{10,15}", message = "Phone number must be between 10-15 digits") String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NotBlank(message = "Phone number is required") @Pattern(regexp = "\\d{10,15}", message = "Phone number must be between 10-15 digits") String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public @NotBlank(message = "Address is required") @Size(max = 255, message = "Address must be at most 255 characters") String getAddress() {
        return address;
    }

    public void setAddress(@NotBlank(message = "Address is required") @Size(max = 255, message = "Address must be at most 255 characters") String address) {
        this.address = address;
    }
}

