package com.dealerapp.facebook.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class VehicleAdRequest {
    private Long vehicleId;
    private String title;
    private String description;
    private Double price;
    private List<String> images;
    private String facebookPageId;
}
