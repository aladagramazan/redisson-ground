package com.rem.redisson.test.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {

    private String id;
    private String city;
    private double latitude;
    private double longitude;
    private String name;
    private String zip;
}
