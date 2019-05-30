package com.mvrcm.repository;

import com.mvrcm.model.Actor;
import com.mvrcm.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActorRepository extends JpaRepository<Actor,Long> {
    Actor findByFullName(String fullName);
}
