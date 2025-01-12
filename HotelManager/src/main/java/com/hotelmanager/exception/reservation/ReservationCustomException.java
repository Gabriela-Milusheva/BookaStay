package com.hotelmanager.exception.reservation;

import java.util.UUID;

import com.hotelmanager.enumerations.reservation.ReservationMessages;

public class ReservationCustomException extends RuntimeException {

    public static class UserNotFoundException extends ReservationCustomException {

        public UserNotFoundException(UUID userId) {
            super(ReservationMessages.USER_NOT_FOUND.format(userId));
        }
    }

    public static class ReservationNotFoundException extends ReservationCustomException {

        public ReservationNotFoundException(UUID reservationId) {
            super(ReservationMessages.RESERVATION_NOT_FOUND.format(reservationId));
        }
    }

    public static class InvalidReservationDateException extends ReservationCustomException {

        public InvalidReservationDateException() {
            super(ReservationMessages.INVALID_DATE_FORMAT.getMessage());
        }
    }

    public static class ReservationConflictException extends ReservationCustomException {

        public ReservationConflictException() {
            super(ReservationMessages.RESERVATION_CONFLICT.getMessage());
        }
    }

    public static class InvalidReservationDataException extends ReservationCustomException {

        public InvalidReservationDataException() {
            super(ReservationMessages.INVALID_RESERVATION_DATA.getMessage());
        }
    }

    public ReservationCustomException(String message) {
        super(message);
    }

    public ReservationCustomException(String message, Throwable cause) {
        super(message, cause);
    }
}
