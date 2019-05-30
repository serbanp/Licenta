package com.mvrcm.service;

import com.mvrcm.model.UserMovieRating;
import com.mvrcm.repository.MovieRepository;
import com.mvrcm.repository.UserMovieRatingRepository;
import com.mvrcm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserMovieRatingService {

    @Autowired
    private UserMovieRatingRepository userMovieRatingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    public List<UserMovieRating> getAll() {
        return userMovieRatingRepository.findAll();
    }

    public UserMovieRating create(Long userId,Long movieId,Short rating) {
        UserMovieRating userMovieRating=new UserMovieRating(userRepository.getOne(userId),movieRepository.getOne(movieId),rating);
        System.out.println(userMovieRating.getMovie().getId());
        System.out.println(userMovieRating.getUser().getId());
        System.out.println(userMovieRating.getRating());
        System.out.println("-------");
        UserMovieRating returnedUserMovieRating=userMovieRatingRepository.save(userMovieRating);
        System.out.println("Rating " +returnedUserMovieRating.getRating());
        System.out.println("User "+ returnedUserMovieRating.getUser().getId());
        System.out.println("Movie " +returnedUserMovieRating.getMovie().getId());
        System.out.println("ID" + returnedUserMovieRating.getId().getMovieId());
        return returnedUserMovieRating;
    }

}
