package com.hotelmanager.exception.requestAndResponse;

import com.hotelmanager.enumerations.requestAndResponse.RequestAndResponseMessages;

public class RequestAndResponseException extends RuntimeException {
    public RequestAndResponseException(String message) {
        super(message);
    }

    public static class RequestIdNullOrEmptyException extends RequestAndResponseException {
        public RequestIdNullOrEmptyException() {
            super(RequestAndResponseMessages.REQUEST_ID_NULL_OR_EMPTY.getMessage());
        }
    }

    public static class ResponseIdNullOrEmptyException extends RequestAndResponseException {
        public ResponseIdNullOrEmptyException() {
            super(RequestAndResponseMessages.RESPONSE_ID_NULL_OR_EMPTY.getMessage());
        }
    }

    public static class SaveRequestAndResponseException extends RequestAndResponseException {
        public SaveRequestAndResponseException(Exception cause) {
            super(RequestAndResponseMessages.REQUEST_AND_RESPONSE_SAVE_ERROR.format(cause.getMessage()));
        }
    }
}
