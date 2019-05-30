package com.mvrcm.repository;

import com.mvrcm.model.Genre;
import com.mvrcm.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreRepository extends JpaRepository<Genre,Long> {
    Genre findByTitle(String title);
}
