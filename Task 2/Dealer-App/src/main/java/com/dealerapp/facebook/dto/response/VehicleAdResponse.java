package com.dealerapp.facebook.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VehicleAdResponse {
    private String adId;
    private String status;
    private String facebookUrl;
}
