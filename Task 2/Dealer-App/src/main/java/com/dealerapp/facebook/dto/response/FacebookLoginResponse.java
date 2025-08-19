package com.dealerapp.facebook.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FacebookLoginResponse {
    private String accessToken;
    private String facebookAccessToken;
    private Long expiresIn;
}
