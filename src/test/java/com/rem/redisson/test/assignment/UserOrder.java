package com.rem.redisson.test.assignment;

import lombok.*;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserOrder {
    private int id;
    private Category category;
}
