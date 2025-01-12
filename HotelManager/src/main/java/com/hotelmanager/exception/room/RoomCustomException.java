package com.hotelmanager.exception.room;

public class RoomCustomException extends RuntimeException {
    public RoomCustomException(String message) {
        super(message);
    }

    public static class RoomNotFoundException extends RoomCustomException {
        public RoomNotFoundException(String message) {
            super(message);
        }
    }

    public static class RoomAlreadyExistsException extends RoomCustomException {
        public RoomAlreadyExistsException(String message) {
            super(message);
        }
    }

    public static class RoomNotAvailableException extends RoomCustomException {
        public RoomNotAvailableException(String message) {
            super(message);
        }
    }
}
