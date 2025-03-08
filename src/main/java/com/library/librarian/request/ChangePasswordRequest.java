package com.library.librarian.request;

import com.library.utils.annotation.ValidPassword;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Builder
@Schema
@Data
public class ChangePasswordRequest {

    @NotBlank(message = "Password couldn't be blank")
    private String currentPassword;
    @NotBlank(message = "Password couldn't be blank")
    @ValidPassword
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String newPassword;

    private String ConfirmPassword;

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return ConfirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        ConfirmPassword = confirmPassword;
    }
}
