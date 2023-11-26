package de.eric.bankingapp.user.model.request;

public record ResetPasswordRequest(
        String oldPassword,
        String newPassword
) {
}
