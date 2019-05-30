package com.mvrcm.repository;

import com.mvrcm.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie,Long> {
    List<Movie> findByGenres_Title(String title);
    Movie findByTitle(String title);
}
