package com.mvrcm.controller;

import com.mvrcm.model.Actor;
import com.mvrcm.model.Genre;
import com.mvrcm.model.Movie;
import com.mvrcm.model.Utils.ActorsWrapper;
import com.mvrcm.model.Utils.GenresWrapper;
import com.mvrcm.repository.MovieRepository;
import com.mvrcm.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/movies")
    public Page<Movie> getMovies(@PageableDefault(sort={"id"}) Pageable pageable) {
        return movieService.getMoviesByPage(pageable);
    }

    @GetMapping("/movies/title/{title}")
    public Movie getByTitle(String title) {
        return movieService.getByTitle(title);
    }

    @GetMapping("/movies/all")
    public List<Movie> getMovies() {
        return movieService.getAll();
    }


    @GetMapping("/movies/{id}")
    public Movie getMovieById(@PathVariable Long id) {return movieService.getById(id);
    }

    @GetMapping("/movies/genres/{title}")
    public Page<Movie> getMoviesByGenreTitle(@PathVariable String title,@PageableDefault(sort={"id"}) Pageable pageable) {return movieService.getMoviesByGenre(title,pageable);}

    @PostMapping("/movies")
    public Movie createMovie(@Valid @RequestBody Movie movie) {
            return movieService.create(movie);
    }

    @PostMapping("/movies/{movieId}/add-actors")
    public Set<Actor> addActors(@PathVariable Long movieId, @Valid @RequestBody ActorsWrapper actorsWrapper) {
        return movieService.addActors(movieId,actorsWrapper);
    }

    @PostMapping("/movies/{movieId}/remove-actors")
    public Set<Actor> removeActors(@PathVariable Long movieId, @Valid @RequestBody ActorsWrapper actorsWrapper) {
        return movieService.removeActors(movieId,actorsWrapper);
    }

    @PostMapping("/movies/{movieId}/add-genres")
    public Set<Genre> addGenres(@PathVariable Long movieId, @Valid @RequestBody GenresWrapper genresWrapper) {
        return movieService.addGenres(movieId,genresWrapper);
    }

    @PostMapping("/movies/{movieId}/remove-genres")
    public Set<Genre> removeGenres(@PathVariable Long movieId, @Valid @RequestBody GenresWrapper genresWrapper) {
        return movieService.removeGenres(movieId,genresWrapper);
    }

    @PutMapping("/movies/{id}")
    public Movie editMovie(@PathVariable Long id,@Valid @RequestBody Movie movie) {
        return movieService.edit(id,movie);
    }

    @PatchMapping("/movies/{id}")
    public Movie patchMovie(@PathVariable Long id,@Valid @RequestBody Map<String, Object> fields) {
        return movieService.patch(id,fields);
    }
    @DeleteMapping("/movies/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable Long id) {
        return movieService.delete(id);
    }

    @GetMapping("/movies/title-contains/{title}")
    public List<Movie> getMoviesTitleContaining(@PathVariable String title) {
        return movieService.getMoviesWithTitleContaining(title);
    }

}
