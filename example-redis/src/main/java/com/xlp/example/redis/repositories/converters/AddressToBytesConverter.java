package com.xlp.example.redis.repositories.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xlp.example.redis.repositories.entity.Address;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.lang.NonNull;

@WritingConverter
public class AddressToBytesConverter implements Converter<Address, byte[]> {

    private final Jackson2JsonRedisSerializer<Address> serializer;

    public AddressToBytesConverter(){
        serializer = new Jackson2JsonRedisSerializer<>(Address.class);
        serializer.setObjectMapper(new ObjectMapper());
    }

    @Override
    public byte[] convert(@NonNull Address address) {
        return serializer.serialize(address);
    }
}
