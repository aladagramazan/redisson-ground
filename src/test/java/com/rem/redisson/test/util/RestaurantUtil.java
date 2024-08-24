package com.rem.redisson.test.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rem.redisson.test.dto.Restaurant;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class RestaurantUtil {

    public static List<Restaurant> getRestaurants() {
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream resourceAsStream = RestaurantUtil.class.getClassLoader().getResourceAsStream("restaurant.json");
        try {
            return objectMapper.readValue(resourceAsStream, new TypeReference<List<Restaurant>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
