package com.mvrcm.service;

import com.mvrcm.model.Genre;
import com.mvrcm.model.Movie;
import com.mvrcm.repository.GenreRepository;
import com.mvrcm.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
                        for (Movie movie:movieRepository.findByGenres_Title(genre.getTitle()))
                            movie.getGenres().remove(genre);
                        genreRepository.delete(genre);
                        return ResponseEntity.ok().build();
                    }).orElseThrow(() -> new Exception("Genre not found with id " + id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}
