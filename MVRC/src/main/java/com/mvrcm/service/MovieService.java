package com.mvrcm.service;

import com.mvrcm.Exceptions.InsideResourceNotFoundException;
import com.mvrcm.Exceptions.ResourceNotFoundException;
import com.mvrcm.model.Actor;
import com.mvrcm.model.Director;
import com.mvrcm.model.Genre;
import com.mvrcm.model.Movie;
import com.mvrcm.repository.ActorRepository;
import com.mvrcm.repository.DirectorRepository;
import com.mvrcm.repository.GenreRepository;
import com.mvrcm.repository.MovieRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private DirectorRepository directorRepository;


    public Page<Movie> getAll(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    public Movie getById(Long id) {
        return movieRepository.getOne(id);
    }

    public List<Movie> getMoviesByGenre(String title) {
        return movieRepository.findByGenres_Title(title);
    }

    @Transactional(rollbackFor = InsideResourceNotFoundException.class)
    public Movie create(Movie movie) {
        saveGenres(movie);
        saveActors(movie);
        saveDirector(movie);
        return movieRepository.save(movie);
    }

    @Transactional(rollbackFor = InsideResourceNotFoundException.class)
    public Movie edit(Long id, Movie requestMovie) {
        try {
            return movieRepository.findById(id)
                    .map(movie -> {
                        movie.setTitle(requestMovie.getTitle());
                        movie.setGenres(requestMovie.getGenres());
                        movie.setActors(requestMovie.getActors());
                        movie.setDirector(requestMovie.getDirector());
                        saveGenres(movie);
                        saveActors(movie);
                        saveDirector(movie);
                        return movieRepository.save(movie);
                    }).orElseThrow(() -> new ResourceNotFoundException("Movie not found with id : " + id));
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    public ResponseEntity<?> delete(Long id) {
        try {
            return movieRepository.findById(id)
                    .map(movie -> {
                        movieRepository.delete(movie);
                        return ResponseEntity.ok().build();
                    }).orElseThrow(() -> new ResourceNotFoundException("Movie not found with id " + id));
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    private void saveGenres(Movie movie) {
        if (movie.getGenres() != null)
            try {
                for (Genre genre : movie.getGenres()) {
                    Genre existingGenre = genreRepository.findById(genre.getId()).orElseThrow(() -> new InsideResourceNotFoundException("Genre not found with id : " + genre.getId() + " inside movie object."));
                    BeanUtils.copyProperties(existingGenre, genre);
                }
            } catch (InsideResourceNotFoundException e) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, e.getMessage(), e);
            }
    }

    private void saveActors(Movie movie) {
        if (movie.getActors() != null)
            try {
                for (Actor actor : movie.getActors()) {
                    Actor existingActor = actorRepository.findById(actor.getId()).orElseThrow(() -> new InsideResourceNotFoundException("Actor not found with id : " + actor.getId() + " inside movie object."));
                    BeanUtils.copyProperties(existingActor, actor);
                }
            } catch (InsideResourceNotFoundException e) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, e.getMessage(), e);
            }
    }

    private void saveDirector(Movie movie) {
        if (movie.getDirector() != null)
            try {
                Director existingDirector = directorRepository.findById(movie.getDirector().getId()).orElseThrow(() -> new InsideResourceNotFoundException("Director not found with id : " + movie.getDirector().getId() + " inside movie object."));
                BeanUtils.copyProperties(existingDirector, movie.getDirector());
            } catch (InsideResourceNotFoundException e) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, e.getMessage(), e);
            }
    }

    public Set<Actor> addActors(Long movieId, Actor[] actors) {
        try {
            Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new ResourceNotFoundException("Movie not found with id " + movieId));
            try {
                for (Actor actor : actors) {
                    Actor foundActor = actorRepository.findById(actor.getId()).orElseThrow(() -> new InsideResourceNotFoundException("Actor not found with id " + actor.getId() + " in actors list."));
                    movie.addActor(foundActor);
                }
                movieRepository.save(movie);
                return movie.getActors();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
        return null;
    }

    public Set<Actor> removeActors(Long movieId, Actor[] actors) {
        try {
            Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new Exception("Movie not found with id " + movieId));
            try {
                for (Actor actor : actors) {
                    Actor foundActor = actorRepository.findById(actor.getId()).orElseThrow(() -> new Exception("Actor not found with id " + actor.getId()));
                    movie.removeActor(foundActor);
                }
                movieRepository.save(movie);
                return movie.getActors();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
