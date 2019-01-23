package com.xlp.example.redis.repositories.config;

import com.xlp.example.redis.repositories.converters.AddressToBytesConverter;
import com.xlp.example.redis.repositories.converters.AddressToMapConverter;
import com.xlp.example.redis.repositories.converters.BytesToAddressConverter;
import com.xlp.example.redis.repositories.converters.MapToAddressConverter;
import com.xlp.example.redis.repositories.index.MyIndexConfiguration;
import com.xlp.example.redis.repositories.keyspaces.MyKeyspaceConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.redis.core.convert.MappingConfiguration;
import org.springframework.data.redis.core.convert.RedisCustomConversions;
import org.springframework.data.redis.core.index.IndexConfiguration;
import org.springframework.data.redis.core.mapping.RedisMappingContext;

import java.util.Arrays;
import java.util.List;

@Configuration
public class RedisRepositoriesConfig {

    @Bean
    public RedisCustomConversions redisCustomConversions(){
        List<Converter> converters = Arrays.asList(
                new AddressToBytesConverter(),
                new BytesToAddressConverter(),
                new AddressToMapConverter(),
                new MapToAddressConverter());
        return new RedisCustomConversions(converters);
    }

    @Bean(name = "keyValueMappingContext")
    public RedisMappingContext keyValueMappingContext(){
        return new RedisMappingContext(
                new MappingConfiguration(
                        new MyIndexConfiguration(),
                        new MyKeyspaceConfiguration()));
    }

}
