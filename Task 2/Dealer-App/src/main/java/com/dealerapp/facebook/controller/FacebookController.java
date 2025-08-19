package com.dealerapp.facebook.controller;

import com.dealerapp.facebook.dto.request.FacebookLoginRequest;
import com.dealerapp.facebook.dto.request.VehicleAdRequest;
import com.dealerapp.facebook.dto.response.FacebookLoginResponse;
import com.dealerapp.facebook.dto.response.VehicleAdResponse;
import com.dealerapp.facebook.jwt.JwtUtil;
import com.dealerapp.facebook.service.FacebookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/facebook")
@RequiredArgsConstructor
public class FacebookController {

    private final FacebookService facebookService;
    private final JwtUtil jwtUtil;

    @PostMapping("/auth/login")
    public ResponseEntity<FacebookLoginResponse> login(@RequestBody FacebookLoginRequest request) {
        FacebookLoginResponse response = facebookService.login(request.getFacebookAuthCode());
        String jwt = jwtUtil.generateToken("dealerUser");
        response.setAccessToken(jwt);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/ads")
    public ResponseEntity<VehicleAdResponse> postAd(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody VehicleAdRequest request) {

        String token = authHeader.replace("Bearer ", "");
        jwtUtil.validateToken(token);

        return ResponseEntity.ok(facebookService.postAd(request));
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<FacebookLoginResponse> refreshToken(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        jwtUtil.validateToken(token);

        FacebookLoginResponse response = facebookService.refreshToken();
        response.setAccessToken(jwtUtil.generateToken("dealerUser"));
        return ResponseEntity.ok(response);
    }
}
