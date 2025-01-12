package com.hotelmanager.enumerations.hotel;

public enum HotelMessages {
    HOTELS_FETCH_SUCCESS("Hotels fetched successfully"),
    CUSTOM_ERROR("An error occurred"),
    HOTEL_CREATED_SUCCESS("Hotel created successfully"),
    HOTEL_DELETED_SUCCESS("Hotel deleted successfully"),
    ROOMS_FETCH_SUCCESS("Rooms fetched successfully"),
    ROOMS_FETCH_FAILED("Failed to fetch rooms"),
    HOTEL_ALREADY_EXISTS("Hotel with name '%s' already exists"),
    HOTEL_CREATION_FAILED("Failed to create hotel"),
    HOTEL_UPDATE_SUCCESS("Hotel updated successfully"),
    HOTEL_UPDATE_FAILED("Failed to update hotel"),
    HOTEL_DELETE_FAILED("Failed to delete hotel"),
    HOTEL_FETCH_SUCCESS("Hotel fetched successfully"),
    REVIEW_ADDED_SUCCESS("Review added successfully"),
    REVIEW_ADD_FAILED("Failed to add review"),
    REVIEW_FETCH_SUCCESS("Reviews fetched successfully"),
    REVIEWS_FETCH_SUCCESS("Reviews fetched successfully"),
    HOTEL_NOT_FOUND("Hotel with ID '%s' not found");

    private final String message;

    HotelMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String format(Object... args) {
        return String.format(message, args);
    }
}
