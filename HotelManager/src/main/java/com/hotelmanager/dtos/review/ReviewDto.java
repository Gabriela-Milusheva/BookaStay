package com.hotelmanager.dtos.review;

import java.io.Serializable;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ReviewDto implements Serializable {
    private String reviewTitle;
    private String reviewDesc;
    private int rating;
    private UUID hotelId;
    //private String userId;
}
