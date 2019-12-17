package com.drinker.adapter;

import androidx.annotation.Nullable;

public class Result<T> {
    private ResultStatus status;

    @Nullable
    private T response;

    @Nullable
    private Exception exception;

    private Result(ResultStatus status, @Nullable T response, @Nullable Exception exception) {
        this.status = status;
        this.response = response;
        this.exception = exception;
    }

    public static <T> Result<T> success(T response) {
        return new Result<>(ResultStatus.SUCCESS, response, null);
    }

    public static <T> Result<T> failure(Exception e) {
        return new Result<>(ResultStatus.FAILURE, null, e);
    }

    public ResultStatus getStatus() {
        return status;
    }

    public void setStatus(ResultStatus status) {
        this.status = status;
    }

    @Nullable
    public T getResponse() {
        return response;
    }

    public void setResponse(@Nullable T response) {
        this.response = response;
    }

    @Nullable
    public Exception getException() {
        return exception;
    }

    public void setException(@Nullable Exception exception) {
        this.exception = exception;
    }
}
