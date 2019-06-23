package com.mvrcm.repository;

import com.mvrcm.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie,Long> {
    @Query("select movie.id from Movie movie")
    List<Long> getAllIds();
    Page<Movie> findByGenres_Title(String title, Pageable pageable);
    List<Movie> findByGenres_Title(String title);
    List<Movie> findByTags_Title(String title);
    Movie findByTitle(String title);
    List<Movie> findAllByOrderByIdAsc();
    List<Movie> findByTitleIgnoreCaseContaining(String title);
}
