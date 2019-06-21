package com.mvrcm.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mvrcm.model.Utils.UserMovieId;

import javax.persistence.*;
import java.io.Serializable;

@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@Entity
@Table(name="users_movies_ratings")
public class UserMovieRating implements Serializable {
    @EmbeddedId
    private UserMovieId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("userId")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("movieId")
    private Movie movie;

    @Column
    private Short rating;

    public UserMovieRating() {}

    public UserMovieRating(User user,Movie movie,Short rating) {
        this.id=new UserMovieId(user.getId(),movie.getId());
        this.user=user;
        this.movie=movie;
        this.rating=rating;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Short getRating() {
        return rating;
    }

    public void setRating(Short rating) {
        this.rating = rating;
    }

    public UserMovieId getId() {
        return id;
    }

    public void setId(UserMovieId id) {
        this.id = id;
    }
}
