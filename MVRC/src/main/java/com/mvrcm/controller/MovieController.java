package com.mvrcm.controller;

import com.mvrcm.model.Actor;
import com.mvrcm.model.Movie;
import com.mvrcm.repository.MovieRepository;
import com.mvrcm.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/movies")
    public Page<Movie> getMovies(Pageable pageable) {
        return movieService.getAll(pageable);
    }

    @GetMapping("/movies/{id}")
    public Movie getMovieById(@PathVariable Long id) {return movieService.getById(id);
    }

    @GetMapping("/movies/genres/{title}")
    public List<Movie> getMoviesByGenreTitle(@PathVariable String title) {return movieService.getMoviesByGenre(title);}

    @PostMapping("/movies")
    public Movie createMovie(@Valid @RequestBody Movie movie) {
            return movieService.create(movie);
    }

    @PostMapping("/movies/{movieId}/add-actors")
    public Set<Actor> addActors(@PathVariable Long movieId, @Valid @RequestBody Actor[] actors) {
        return movieService.addActors(movieId,actors);
    }

    @PostMapping("/movies/{movieId}/remove-actors")
    public Set<Actor> removeActors(@PathVariable Long movieId, @Valid @RequestBody Actor[] actors) {
        return movieService.removeActors(movieId,actors);
    }

    @PutMapping("/movies/{id}")
    public Movie editMovie(@PathVariable Long id,@Valid @RequestBody Movie movie) {
        return movieService.edit(id,movie);
    }


    @DeleteMapping("/movies/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable Long id) {
        return movieService.delete(id);
    }


}
