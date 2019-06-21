package com.mvrcm.repository;

import com.mvrcm.model.UserMovieRating;
import com.mvrcm.model.Utils.UserMovieId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserMovieRatingRepository extends JpaRepository<UserMovieRating,UserMovieId> {
    List<UserMovieRating> findByUser_username(String username);
    @Query("SELECT AVG(userMovieRating.rating) FROM UserMovieRating userMovieRating WHERE userMovieRating.movie.id=:movieId")
    Double getMovieAverageRating(@Param("movieId") Long movieId);
    UserMovieRating findByUser_idAndMovie_id(Long userId,Long movieId);

}
