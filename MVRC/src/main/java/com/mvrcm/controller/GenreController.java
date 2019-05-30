package com.mvrcm.controller;

import com.mvrcm.model.Genre;
import com.mvrcm.model.Movie;
import com.mvrcm.service.GenreService;
import com.mvrcm.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class GenreController {

   @Autowired
   private GenreService genreService;

   @Autowired
   private MovieService movieService;

   @GetMapping("/genres/{id}")
   public Genre getGenreById(@PathVariable Long id) {return genreService.getById(id);}

   @GetMapping("/genres")
   public List<Genre> getGenres() {
        return genreService.getAll();
    }


    @PostMapping("/genres")
    public Genre createGenre(@Valid @RequestBody Genre genre) {
        return genreService.create(genre);
    }

   @DeleteMapping("/genres/{id}")
   public ResponseEntity<?> deleteGenre(@PathVariable Long id) {
       return genreService.delete(id);
   }




}
