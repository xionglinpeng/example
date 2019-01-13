package com.xlp.example.redis.repositories.converters;

import com.xlp.example.redis.repositories.entity.Address;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.lang.NonNull;

import java.util.Collections;
import java.util.Map;

@WritingConverter
public class AddressToMapConverter implements Converter<Address, Map<String,byte[]>> {

    @Override
    public Map<String, byte[]> convert(@NonNull Address address) {
        return Collections.singletonMap("ciudad",address.getCity().getBytes());
    }
}
