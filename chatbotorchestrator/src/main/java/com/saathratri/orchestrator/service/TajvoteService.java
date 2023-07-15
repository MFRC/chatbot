package com.saathratri.orchestrator.service;

import java.util.UUID;

import com.saathratri.tajvote.service.dto.LandingPageByOrganizationDTO;
import com.saathratri.tajvote.service.dto.CustomerReviewsByHotelAndLastNameAndFirstNameDTO;
import com.saathratri.tajvote.service.dto.RatingByHotelAndReviewDTO;

import reactor.core.publisher.Mono;

import com.saathratri.tajvote.service.dto.CustomerReviewsByHotelAndMainStarRatingDTO;
import com.saathratri.tajvote.service.dto.CustomerReviewsByHotelDTO;

public interface TajvoteService {
 
    Mono<CustomerReviewsByHotelAndLastNameAndFirstNameDTO> update(CustomerReviewsByHotelAndLastNameAndFirstNameDTO customerReview);

    Mono<LandingPageByOrganizationDTO> findLandingPageByOrganizationId(UUID id);

    Mono<CustomerReviewsByHotelAndLastNameAndFirstNameDTO> createReviewsByHotelAndLastNameAndFirstName(CustomerReviewsByHotelAndLastNameAndFirstNameDTO customerReview);
 
    Mono<RatingByHotelAndReviewDTO> createRatingByHotelAndReview(RatingByHotelAndReviewDTO customerRating);

	Mono<CustomerReviewsByHotelAndMainStarRatingDTO> createCustomerReviewsByHotelAndMainStarRating(CustomerReviewsByHotelAndMainStarRatingDTO customerReview);

    Mono<CustomerReviewsByHotelDTO> createCustomerReviewsByHotel(CustomerReviewsByHotelDTO customerReview);
}
