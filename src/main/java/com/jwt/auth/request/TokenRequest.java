package com.jwt.auth.request;

import lombok.Data;

@Data
public class TokenRequest {

    private String refreshToken;
    private String accessToken;

}
