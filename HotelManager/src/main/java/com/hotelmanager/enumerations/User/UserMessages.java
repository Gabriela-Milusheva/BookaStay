package com.hotelmanager.enumerations.User;

public enum UserMessages {
    INVALID_ID("Invalid ID."),
    USER_NOT_FOUND("User not found."),
    EMAIL_ALREADY_EXISTS("Email is already registered."),
    INVALID_PASSWORD("The password is invalid."),
    USER_NOT_ACTIVE("User account is not active."),
    USERNAME_ALREADY_EXISTS("Username is already taken."),
    USER_ALREADY_EXISTS("User already exists."),
    USER_LOCKED("User account is locked."),
    PASSWORD_TOO_SHORT("Password must be at least 6 characters long."),
    INVALID_EMAIL_FORMAT("The email format is invalid."),
    USER_NOT_VERIFIED("User account is not verified."),
    PASSWORD_MISMATCH("The provided passwords do not match."),
    USER_ALREADY_LOGGED_IN("User is already logged in."),
    ROLE_NOT_FOUND("User role not found."),
    USER_DELETED("User account has been deleted."),
    REGISTER_FAILED("Failed to register user"),
    REGISTER_SUCCESS("User registered successfully"),
    PASSWORD_CHANGED_SUCCESS("Password was changed successfully"),
    USER_UPDATE_SUCCESS("User was updated successfully"),
    INVALID_EMAIL("Invalid email."),
    LOGIN_SUCCESS("User logged in successfully."),
    USERS_FETCH_SUCCESS("Users fetched successfully."),
    USER_FETCH_SUCCESS("User fetched successfully.");

    private final String message;

    UserMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
