package com.hotelmanager.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hotelmanager.dtos.User.UserDto;
import com.hotelmanager.models.User;

@Configuration
public class ModelMapperConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true);

        TypeMap<User, UserDto> typeMap = modelMapper.createTypeMap(User.class, UserDto.class);

        return modelMapper;
    }
}
