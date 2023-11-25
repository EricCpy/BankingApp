package de.eric.bankingapp.user.model;

public record UserResponse (
        String email,
        String firstName,
        String lastName,
        UserRole role,
        boolean blocked,
        boolean emailVerified
){
    public UserResponse(User user) {
        this(user.getEmail(), user.getFirstName(), user.getLastName(), user.getRole(), user.isBlocked(), user.isEmailVerified());
    }
}
