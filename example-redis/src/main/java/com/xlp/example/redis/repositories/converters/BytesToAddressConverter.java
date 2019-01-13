package com.xlp.example.redis.repositories.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xlp.example.redis.repositories.entity.Address;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.lang.NonNull;

@ReadingConverter
public class BytesToAddressConverter implements Converter<byte[], Address> {

    private final Jackson2JsonRedisSerializer<Address> serializer;

    public BytesToAddressConverter(){
        serializer = new Jackson2JsonRedisSerializer<>(Address.class);
        serializer.setObjectMapper(new ObjectMapper());
    }

    @Override
    public Address convert(@NonNull byte[] address) {
        return serializer.deserialize(address);
    }
}
