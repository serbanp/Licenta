package com.mvrcm.service;

import com.mvrcm.exceptions.InsideResourceNotFoundException;
import com.mvrcm.exceptions.ResourceNotFoundException;
import com.mvrcm.model.*;
import com.mvrcm.model.Utils.ActorsWrapper;
import com.mvrcm.model.Utils.GenresWrapper;
import com.mvrcm.repository.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private TagRepository tagRepository;

    public Page<Movie> getMoviesByPage(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    public List<Movie> getAll() {return movieRepository.findAllByOrderByIdAsc();}

    public Movie getById(Long id) {
        return movieRepository.getOne(id);
    }

    public Movie getByTitle(String title) {return movieRepository.findByTitle(title) ;}

    public Page<Movie> getMoviesByGenre(String title,Pageable pageable) {
        return movieRepository.findByGenres_Title(title,pageable);
    }

    public List<Movie> getMoviesWithTitleContaining(String title) {
        return movieRepository.findByTitleIgnoreCaseContaining(title);
    }

    public List<Long> getAllMoviesIds() {return movieRepository.getAllIds();}

    @Transactional(rollbackFor = InsideResourceNotFoundException.class)
    public Movie create(Movie movie) {
        saveGenres(movie);
        saveTags(movie);
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
                        movie.setTags(requestMovie.getTags());
                        movie.setActors(requestMovie.getActors());
                        movie.setDirector(requestMovie.getDirector());
                        movie.setBio(requestMovie.getBio());
                        movie.setReleaseDate(requestMovie.getReleaseDate());
                        movie.setImgSrc(requestMovie.getImgSrc());
                        saveGenres(movie);
                        saveActors(movie);
                        saveDirector(movie);
                        saveTags(movie);
                        return movieRepository.save(movie);
                    }).orElseThrow(() -> new ResourceNotFoundException("Movie not found with id : " + id));
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    public Movie patch(Long id, Map<String, Object> fields) {
        try {
            Movie movie = movieRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id : " + id));
            ;
            fields.remove("id");
            fields.forEach((k, v) -> {
                Field field = ReflectionUtils.findField(Movie.class, k);
                field.setAccessible(true);
                ReflectionUtils.setField(field, movie, v);
            });
            System.out.println(movie.getId()+" "+movie.getTitle());
            return movieRepository.save(movie);
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

    private void saveTags(Movie movie) {
        if (movie.getTags() != null)
            try {
                for (Tag tag : movie.getTags()) {
                    Tag existingTag = tagRepository.findById(tag.getId()).orElseThrow(() -> new InsideResourceNotFoundException("Tag not found with id : " + tag.getId() + " inside movie object."));
                    BeanUtils.copyProperties(existingTag, tag);
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

    public Set<Actor> addActors(Long movieId, ActorsWrapper actorsWrapper) {
        try {
            Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new ResourceNotFoundException("Movie not found with id " + movieId));
            try {
                System.out.println(actorsWrapper.getActors().size());
                for (Actor actor : actorsWrapper.getActors()) {
                    Actor foundActor = actorRepository.findById(actor.getId()).orElseThrow(() -> new InsideResourceNotFoundException("Actor not found with id " + actor.getId() + " in actors list."));
                    movie.addActor(foundActor);
                    System.out.println(foundActor.getId());
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

    public Set<Genre> addGenres(Long movieId, GenresWrapper genresWrapper) {
        try {
            Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new ResourceNotFoundException("Movie not found with id " + movieId));
            try {
                System.out.println(genresWrapper.getGenres().size());
                for (Genre genre : genresWrapper.getGenres()) {
                    Genre foundGenre = genreRepository.findById(genre.getId()).orElseThrow(() -> new InsideResourceNotFoundException("Actor not found with id " + genre.getId() + " in actors list."));
                    movie.addGenre(foundGenre);
                    System.out.println(foundGenre.getId());
                }
                movieRepository.save(movie);
                return movie.getGenres();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
        return null;
    }

    public Set<Genre> removeGenres(Long movieId, GenresWrapper genresWrapper) {
        try {
            Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new ResourceNotFoundException("Movie not found with id " + movieId));
            System.out.println(movie.getGenres().size());
            try {
                System.out.println(genresWrapper.getGenres().size());
                for (Genre genre : genresWrapper.getGenres()) {
                    Genre foundGenre = genreRepository.findById(genre.getId()).orElseThrow(() -> new InsideResourceNotFoundException("Actor not found with id " + genre.getId() + " in actors list."));
                    movie.removeGenre(foundGenre);
                }
                System.out.println(movie.getGenres().size());
                movieRepository.save(movie);
                return movie.getGenres();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
        return null;
    }

    public Set<Actor> removeActors(Long movieId, ActorsWrapper actorsWrapper) {
        try {
            Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new Exception("Movie not found with id " + movieId));
            try {
                for (Actor actor : actorsWrapper.getActors()) {
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
