package com.example.demo.security;

public class SecurityConstants {
    static final long EXPIRATION_TIME = 1000 * 60 * 24 * 10; // last value is number of days, so 10 days
    static final String SECRET = "haveToTellYouOne";
    static final String TOKEN_PREFIX = "Bearer ";
    static final String HEADER_STRING = "Authorization";
    static final String SIGN_UP_URL = "/api/user/create";
}
