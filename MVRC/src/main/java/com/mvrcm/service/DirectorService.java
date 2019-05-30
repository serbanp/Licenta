package com.mvrcm.service;

import com.mvrcm.Exceptions.ResourceNotFoundException;
import com.mvrcm.model.Director;
import com.mvrcm.repository.DirectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class DirectorService {
    @Autowired
    private DirectorRepository directorRepository;

    public Director getById(Long id) {return directorRepository.getOne(id);}

    public Director getByFullName(String fullName) {return directorRepository.findByFullName(fullName);}

    public List<Director> getAll() {return directorRepository.findAll();}

    public Director create(Director director) {
        if (directorRepository.findByFullName(director.getFullName())!=null)
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Director with name " + director.getFullName()+ " already exists.");
        return directorRepository.save(director);
    }

    public Director edit(Long id,Director requestDirector) {
        try {
            return directorRepository.findById(id)
                    .map(director -> {
                        director.setFullName(requestDirector.getFullName());
                        director.setBirthday(requestDirector.getBirthday());
                        director.setBio(requestDirector.getBio());
                        return directorRepository.save(director);
                    }).orElseThrow(() -> new ResourceNotFoundException("Actor not found with id " + id));
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    public ResponseEntity<?> delete(Long id) {
        try {
            return directorRepository.findById(id)
                    .map(director -> {
                        directorRepository.delete(director);
                        return ResponseEntity.ok().build();
                    }).orElseThrow(() -> new ResourceNotFoundException("Director not found with id " + id));
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

}
