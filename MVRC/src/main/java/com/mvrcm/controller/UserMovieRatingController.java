package com.mvrcm.controller;

import com.mvrcm.model.UserMovieRating;
import com.mvrcm.service.UserMovieRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserMovieRatingController {
    @Autowired
    private UserMovieRatingService userMovieRatingService;

    @PostMapping("/rate/{userId}/{movieId}/{rating}")
    public UserMovieRating createUserMovieRating(@PathVariable Long movieId,@PathVariable Long userId,@PathVariable Short rating) {
        return userMovieRatingService.create(userId,movieId,rating);
    }

}
