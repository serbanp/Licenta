package com.mvrcm.service;

import com.mvrcm.exceptions.ResourceNotFoundException;
import com.mvrcm.model.Genre;
import com.mvrcm.model.Movie;
import com.mvrcm.repository.GenreRepository;
import com.mvrcm.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private MovieRepository movieRepository;

    public List<Genre> getAll() {
        return genreRepository.findAll();
    }

    public Genre getById(Long id) {
        return genreRepository.getOne(id);
    }

    public Genre getByTitle(String title) {
        return genreRepository.findByTitle(title);
    }

    public Genre create(Genre genre) {
        return genreRepository.save(genre);
    }

    public ResponseEntity<?> delete(Long id) {
        try {
            return genreRepository.findById(id)
                    .map(genre -> {
                        for (Movie movie : movieRepository.findByGenres_Title(genre.getTitle()))
                            movie.getGenres().remove(genre);
                        genreRepository.delete(genre);
                        return ResponseEntity.ok().build();
                    }).orElseThrow(() -> new ResourceNotFoundException("Genre not found with id " + id));
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}



