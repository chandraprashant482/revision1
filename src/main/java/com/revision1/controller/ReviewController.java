package com.revision1.controller;

import com.revision1.enity.Property;
import com.revision1.enity.Reviews;
import com.revision1.enity.User;
import com.revision1.payload.ReviewDto;
import com.revision1.reposistory.PropertyRepository;
import com.revision1.reposistory.ReviewsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/review")
public class ReviewController {

    private PropertyRepository propertyRepository;
    private ReviewsRepository reviewsRepository;
    public ReviewController(PropertyRepository propertyRepository, ReviewsRepository reviewsRepository) {
        this.propertyRepository = propertyRepository;
        this.reviewsRepository = reviewsRepository;
    }
    @PostMapping("/addreview")
    public String addReview(
            @RequestBody Reviews reviews,
            @RequestParam long propertyId,
            //now for user details we will not supply user id the user which is currently logged in will automatically be added
            @AuthenticationPrincipal User user)
    {

        Property property = propertyRepository.findById(propertyId).get();
        Reviews reviewStatus = reviewsRepository.findByPropertyAndUser(property, user);
        if(reviewStatus!=null){
            return "Review already exists for this property and user";
        }
        reviews.setUser(user);
        reviews.setProperty(property);
        reviewsRepository.save(reviews);
        return "Review record for " + propertyId;
    }

    @GetMapping("/seeReview")
    public ResponseEntity<?> viewMyReviews(@AuthenticationPrincipal User user) {
        List<Reviews> reviewsList = reviewsRepository.findAllByUser(user);

        if (reviewsList.isEmpty()) {
            return new ResponseEntity<>("No reviews found", HttpStatus.NOT_FOUND);
        }

        List<ReviewDto> reviewDtos = new ArrayList<>();

        for (Reviews reviews : reviewsList) {
            ReviewDto reviewDto = new ReviewDto();
            reviewDto.setDescription(reviews.getDescription());
            reviewDto.setRating(reviews.getRating());
            reviewDto.setHotelNmae(reviews.getProperty().getName());
            reviewDto.setCity(reviews.getProperty().getCity().getName());
            reviewDto.setCountry(reviews.getProperty().getCountry().getName());

            reviewDtos.add(reviewDto);
        }

        return new ResponseEntity<>(reviewDtos, HttpStatus.OK);
    }

}
