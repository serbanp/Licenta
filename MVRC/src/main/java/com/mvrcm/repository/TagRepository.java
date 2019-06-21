package com.mvrcm.repository;

import com.mvrcm.model.Genre;
import com.mvrcm.model.Movie;
import com.mvrcm.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag,Long> {
    Tag findByTitle(String title);
}
