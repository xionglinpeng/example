package com.xlp.example.redis.repositories.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.index.Indexed;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    private String country;

    @Indexed
    private String city;

}
