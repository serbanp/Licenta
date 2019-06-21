package com.mvrcm.controller;

import com.mvrcm.model.UserMovieRating;
import com.mvrcm.model.Utils.AvgRatingWrapper;
import com.mvrcm.model.Utils.UserMovieRatingWrapper;
import com.mvrcm.service.UserMovieRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Path;
import java.util.List;

@RestController
public class UserMovieRatingController {
    @Autowired
    private UserMovieRatingService userMovieRatingService;

    @PostMapping("/rate/{userId}/{movieId}/{rating}")
    public UserMovieRating createUserMovieRating(@PathVariable Long movieId,@PathVariable Long userId,@PathVariable Short rating) {
        return userMovieRatingService.create(userId,movieId,rating);
    }

    @GetMapping("/ratings/{username}")
    public List<UserMovieRatingWrapper> getRatingsForUser(@PathVariable String username) {
        return userMovieRatingService.getRatingsForUser(username);
    }
    @GetMapping("/ratings/{username}/{movieId}")
    public UserMovieRatingWrapper getRatingForMovie(@PathVariable String username,@PathVariable Long movieId) {
        return userMovieRatingService.getRatingForMovie(username,movieId);
    }

    @GetMapping("/ratings/avg/{movieId}")
    public AvgRatingWrapper getAvgRatingForMovie(@PathVariable Long movieId) {
        return userMovieRatingService.getAverageRatingForMovie(movieId);
    }

}
