package com.hotelmanager.enumerations.room;

public enum RoomMessages {
    ROOMS_FETCH_SUCCESS("Rooms fetched successfully."),
    ROOM_FETCH_SUCCESS("Room fetched successfully."),
    ROOM_CREATED_SUCCESS("Room created successfully."),
    ROOM_DELETED_SUCCESS("Room deleted successfully."),
    ROOM_CREATION_FAILED("Room creation failed."),
    CUSTOM_ERROR("An error occurred."),
    ROOM_DELETED_FAILD("Room deletion failed."),
    ROOM_UPDATE_FAILED("Room update failed."),
    ROOMS_FETCH_FAILED("Rooms fetch failed."),
    ROOM_UPDATE_SUCCESS("Room updated successfully."),
    ROOM_ALREADY_EXISTS("Room with number %s already exists in hotel with ID %s."),
    ROOM_NOT_FOUND("Room with ID %s not found.");

    private final String message;

    RoomMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
