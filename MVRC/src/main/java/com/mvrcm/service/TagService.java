package com.mvrcm.service;


import com.mvrcm.Exceptions.ResourceNotFoundException;
import com.mvrcm.model.Movie;
import com.mvrcm.model.Tag;
import com.mvrcm.repository.MovieRepository;
import com.mvrcm.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private MovieRepository movieRepository;

    public List<Tag> getAll() {
        return tagRepository.findAll();
    }

    public Tag getById(Long id) {
        return tagRepository.getOne(id);
    }

    public Tag getByTitle(String title) {
        return tagRepository.findByTitle(title);
    }
    public Tag create(Tag tag) {
        return tagRepository.save(tag);
    }

    public ResponseEntity<?> delete(Long id) {
        try {
            return tagRepository.findById(id)
                    .map(tag -> {
                        for (Movie movie : movieRepository.findByTags_Title(tag.getTitle()))
                            movie.getTags().remove(tag);
                        tagRepository.delete(tag);
                        return ResponseEntity.ok().build();
                    }).orElseThrow(() -> new ResourceNotFoundException("Tag not found with id " + id));
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}

