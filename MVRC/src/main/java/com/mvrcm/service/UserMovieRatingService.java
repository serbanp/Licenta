package com.mvrcm.service;

import com.mvrcm.exceptions.ResourceNotFoundException;
import com.mvrcm.model.Movie;
import com.mvrcm.model.User;
import com.mvrcm.model.UserMovieRating;
import com.mvrcm.model.Utils.AvgRatingWrapper;
import com.mvrcm.model.Utils.UserMovieRatingWrapper;
import com.mvrcm.recommender.utils.RatingsDataModel;
import com.mvrcm.repository.MovieRepository;
import com.mvrcm.repository.UserMovieRatingRepository;
import com.mvrcm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserMovieRatingService {

    @Autowired
    private UserMovieRatingRepository userMovieRatingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private RatingsDataModel ratingsDataModel;

    public List<UserMovieRating> getAll() {
        return userMovieRatingRepository.findAll();
    }

    public List<UserMovieRatingWrapper> getRatingsForUser(String username) {
        List<UserMovieRatingWrapper> userMovieRatingWrappers = new ArrayList<>();
        List<UserMovieRating> userMovieRatings = userMovieRatingRepository.findByUser_username(username);
        for (UserMovieRating userMovieRating : userMovieRatings) {
            userMovieRatingWrappers.add(new UserMovieRatingWrapper(userMovieRating.getId().getUserId(), userMovieRating.getId().getMovieId(), userMovieRating.getRating()));
        }
        return userMovieRatingWrappers;
    }

    public UserMovieRatingWrapper getRatingForMovie(String username,Long movieId) {
        User user=userRepository.findByUsername(username);
        UserMovieRating userMovieRating=userMovieRatingRepository.findByUser_idAndMovie_id(user.getId(),movieId);
        if (userMovieRating!=null)
            return new UserMovieRatingWrapper(userMovieRating.getId().getUserId(), userMovieRating.getId().getMovieId(), userMovieRating.getRating());
        else throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "User hasn't rated that movie yet", new ResourceNotFoundException("User hasn't rated that movie yet"));
    }


    public UserMovieRating create(Long userId, Long movieId, Short rating) {
        Movie movie = this.movieRepository.findById(movieId).orElse(null);
        UserMovieRating userMovieRating = new UserMovieRating(userRepository.getOne(userId), movieRepository.getOne(movieId), rating);
        userMovieRatingRepository.save(userMovieRating);
        ratingsDataModel.refresh();
        Double averageRating = getAverageRatingForMovie(movieId).getRating();
        movie.setAverageRating(averageRating);
        movieRepository.save(movie);
        return userMovieRating;
    }

    public AvgRatingWrapper getAverageRatingForMovie(Long movieId) {
        return new AvgRatingWrapper(movieId, userMovieRatingRepository.getMovieAverageRating(movieId));
    }

}
