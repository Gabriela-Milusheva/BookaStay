package com.hotelmanager.enumerations.requestAndResponse;

public enum RequestAndResponseMessages {
    REQUEST_ID_NULL_OR_EMPTY("Request ID cannot be null or empty"),
    RESPONSE_ID_NULL_OR_EMPTY("Response ID cannot be null or empty"),
    REQUEST_AND_RESPONSE_SAVE_ERROR("Error saving request and response: %s");

    private final String message;

    RequestAndResponseMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String format(Object... args) {
        return String.format(message, args);
    }
}
