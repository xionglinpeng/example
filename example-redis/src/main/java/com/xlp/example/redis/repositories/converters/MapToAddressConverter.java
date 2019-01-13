package com.xlp.example.redis.repositories.converters;

import com.xlp.example.redis.repositories.entity.Address;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.lang.NonNull;

import java.util.Map;

@ReadingConverter
public class MapToAddressConverter implements Converter<Map<String,byte[]>, Address> {

    @Override
    public Address convert(@NonNull Map<String, byte[]> source) {
        return new Address("china",new String(source.get("ciudad")));
    }
}
