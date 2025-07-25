package com.raii.jwtauth.api.v1.auth.exceptions;

public class NotValidRefreshTokenException extends Exception {
    public NotValidRefreshTokenException() {
        super("Not valid refresh token provided");
    }
}
