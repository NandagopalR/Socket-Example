package com.nanda.socketexample.data.exception;

import retrofit2.HttpException;
import retrofit2.Response;


public class ApiHttpException extends HttpException {
    public ApiHttpException(Response<?> response) {
        super(response);
    }
}
