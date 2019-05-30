package com.mvrcm.repository;

import com.mvrcm.model.UserMovieRating;
import com.mvrcm.model.Utils.UserMovieId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMovieRatingRepository extends JpaRepository<UserMovieRating,UserMovieId> {
}
