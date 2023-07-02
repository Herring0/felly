package com.herring.felly.payload.response;

public class MessageResponse {

    private String clientId;
    private int code;
    private String message;

    public MessageResponse(String clientId, int code, String message) {
        this.clientId = clientId;
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
