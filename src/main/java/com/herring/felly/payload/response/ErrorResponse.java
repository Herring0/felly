package com.herring.felly.payload.response;

public class ErrorResponse {

    private int code;
    private String error;

    public ErrorResponse(int code, String error) {
        this.code = code;
        this.error = error;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String message) {
        this.error = message;
    }
}
