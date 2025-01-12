package com.hotelmanager.exception.hotel;

import com.hotelmanager.enumerations.hotel.HotelMessages;
import com.hotelmanager.exception.room.RoomCustomException;

public class HotelCustomException extends RuntimeException {

    public HotelCustomException(String message) {
        super(message);
    }

    public static class HotelNotFoundException extends RoomCustomException {

        public HotelNotFoundException(String message) {
            super(message);
        }
    }

    public static class HotelAlreadyExistsException extends HotelCustomException {

        public HotelAlreadyExistsException(String name) {
            super(HotelMessages.HOTEL_ALREADY_EXISTS.format(name));
        }
    }

    public static class HotelCreationFailedException extends HotelCustomException {

        public HotelCreationFailedException() {
            super(HotelMessages.HOTEL_CREATION_FAILED.getMessage());
        }
    }

    public static class HotelUpdateFailedException extends HotelCustomException {

        public HotelUpdateFailedException() {
            super(HotelMessages.HOTEL_UPDATE_FAILED.getMessage());
        }
    }

    public static class HotelDeletionFailedException extends HotelCustomException {

        public HotelDeletionFailedException() {
            super(HotelMessages.HOTEL_DELETE_FAILED.getMessage());
        }
    }
}
