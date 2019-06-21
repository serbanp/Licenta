package com.mvrcm.model.Utils;


import java.math.RoundingMode;
import java.text.DecimalFormat;

public class AvgRatingWrapper {
    private Long movieId;
    private Double rating;

    public AvgRatingWrapper() {}

    public AvgRatingWrapper(Long movieId, Double rating) {
        this.movieId = movieId;
        DecimalFormat df = new DecimalFormat("#.#");
        df.setRoundingMode(RoundingMode.FLOOR);
        this.rating=Double.valueOf(df.format(rating));
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
