package com.dealerapp.facebook.service;

import com.dealerapp.facebook.dto.request.VehicleAdRequest;
import com.dealerapp.facebook.dto.response.FacebookLoginResponse;
import com.dealerapp.facebook.dto.response.VehicleAdResponse;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FacebookService {

    private final Map<String, String> tokenStore = new HashMap<>();

    public FacebookLoginResponse login(String authCode) {
        String fbLongLivedToken = "mock_fb_token_" + authCode;
        tokenStore.put("fbToken", fbLongLivedToken);

        String jwt = "mock_jwt_token";

        return new FacebookLoginResponse(jwt, fbLongLivedToken, 60L * 60 * 24 * 60); // 60 days
    }

    public VehicleAdResponse postAd(VehicleAdRequest request) {
        String fbToken = tokenStore.get("fbToken");
        if (fbToken == null) {
            throw new RuntimeException("Facebook token missing or expired");
        }

        String adId = "mock_ad_" + request.getVehicleId();
        String fbUrl = "https://facebook.com/ads/" + adId;

        return new VehicleAdResponse(adId, "posted", fbUrl);
    }

    public FacebookLoginResponse refreshToken() {
        String newToken = "mock_fb_token_refreshed";
        tokenStore.put("fbToken", newToken);
        return new FacebookLoginResponse("mock_jwt_token", newToken, 60L * 60 * 24 * 60);
    }
}
