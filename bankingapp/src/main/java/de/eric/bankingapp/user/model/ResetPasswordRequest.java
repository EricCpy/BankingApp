package de.eric.bankingapp.user.model;

public record ResetPasswordRequest(
        String oldPassword,
        String newPassword
) {
}
