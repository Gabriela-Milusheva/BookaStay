package com.hotelmanager.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import com.hotelmanager.dtos.review.ReviewDto;
import com.hotelmanager.models.Hotel;
import com.hotelmanager.models.Review;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReviewMapper {

    // Map Review entity to ReviewDto
    @Mapping(target = "reviewTitle", source = "review.reviewTitle")
    @Mapping(target = "reviewDesc", source = "review.reviewDesc")
    @Mapping(target = "rating", source = "review.rating")
    @Mapping(target = "hotelId", source = "review.hotel.id")
    ReviewDto toDto(Review review);

    // Map ReviewDto back to Review entity
    @Mapping(target = "reviewTitle", source = "dto.reviewTitle")
    @Mapping(target = "reviewDesc", source = "dto.reviewDesc")
    @Mapping(target = "rating", source = "dto.rating")
    @Mapping(target = "hotel.id", source = "dto.hotelId")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Review toEntity(ReviewDto dto);
}
