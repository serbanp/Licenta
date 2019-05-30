package com.mvrcm.repository;

import com.mvrcm.model.Director;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectorRepository extends JpaRepository<Director,Long> {
    Director findByFullName(String fullName);
}
