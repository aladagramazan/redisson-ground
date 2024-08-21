package com.rem.redisson.test.dto;

import lombok.*;

import java.util.List;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    private String name;
    private int age;
    private String city;
    private List<Integer> marks;
}
