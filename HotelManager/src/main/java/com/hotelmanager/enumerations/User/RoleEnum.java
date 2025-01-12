package com.hotelmanager.enumerations.User;

public enum RoleEnum {
    USER("USER"),
    ADMIN("ADMIN"),
    MANAGER("HOTEL_MANAGER");

    private final String roleName;

    RoleEnum(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
