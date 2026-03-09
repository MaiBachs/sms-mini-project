package com.sms.dto.response;

public class GeneralResponse<T> {
    private T data;
    private String error;
    // getters setters
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}
