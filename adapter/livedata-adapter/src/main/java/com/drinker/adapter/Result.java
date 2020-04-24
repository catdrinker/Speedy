package com.drinker.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class Result<T> {
    private ResultStatus status;

    @Nullable
    private T response;

    @Nullable
    private Throwable exception;

    private Result(ResultStatus status, @Nullable T response, @Nullable Throwable exception) {
        this.status = status;
        this.response = response;
        this.exception = exception;
    }

    static <T> Result<T> success(T response) {
        return new Result<>(ResultStatus.SUCCESS, response, null);
    }

    static <T> Result<T> failure(Throwable e) {
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
    public Throwable getException() {
        return exception;
    }


    public boolean isSuccess() {
        return this.status == ResultStatus.SUCCESS;
    }

    public boolean isFailure() {
        return this.status == ResultStatus.FAILURE;
    }


    public void setException(@Nullable Exception exception) {
        this.exception = exception;
    }

    @NonNull
    @Override
    public String toString() {
        return "status = " + status
                + " response = " + response
                + " exception = " + exception;
    }
}
