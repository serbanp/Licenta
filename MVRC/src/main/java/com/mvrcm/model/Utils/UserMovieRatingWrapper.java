package com.mvrcm.model.Utils;

public class UserMovieRatingWrapper {
    private Long userId;
    private Long movieId;
    private Short rating;

    public UserMovieRatingWrapper(Long userId, Long movieId, Short rating) {
        this.userId = userId;
        this.movieId = movieId;
        this.rating = rating;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public Short getRating() {
        return rating;
    }

    public void setRating(Short rating) {
        this.rating = rating;
    }
}
