package com.hotelmanager.enumerations.reservation;

public enum ReservationMessages {
    USER_NOT_FOUND("User with ID '%s' not found"),
    RESERVATION_NOT_FOUND("Reservation with ID '%s' not found"),
    RESERVATION_SUCCESS("Reservation was successful"),
    RESERVATION_DELETE_SUCCESS("Reservation with ID '%s' was deleted successfully"),
    RESERVATION_UPDATE_SUCCESS("Reservation with ID '%s' was updated successfully"),
    INVALID_DATE_FORMAT("Invalid date format"),
    RESERVATION_CONFLICT("Reservation conflict"),
    INVALID_RESERVATION_DATA("Invalid reservation data"),
    UNEXPECTED_ERROR("An unexpected error occurred");

    private final String message;

    ReservationMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String format(Object... args) {
        return String.format(message, args);
    }
}
