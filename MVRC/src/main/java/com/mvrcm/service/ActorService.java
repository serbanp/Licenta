package com.mvrcm.service;

import com.mvrcm.exceptions.InsideResourceNotFoundException;
import com.mvrcm.exceptions.ResourceNotFoundException;
import com.mvrcm.model.Actor;
import com.mvrcm.model.Movie;
import com.mvrcm.repository.ActorRepository;
import com.mvrcm.repository.MovieRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ActorService {

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private MovieRepository movieRepository;

    public List<Actor> getAll() {
        return actorRepository.findAll();
    }

    @Transactional(rollbackFor = InsideResourceNotFoundException.class)
    public Actor create(Actor actor) {
        if (actorRepository.findByFullName(actor.getFullName())!=null)
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Actor with name " + actor.getFullName() +  " already exists.");
        Actor savedActor=actorRepository.save(actor);
        saveMovies(savedActor);
        return savedActor;
    }

    public Actor getById(Long id) {
        return actorRepository.getOne(id);
    }

    public Actor getByFullName(String fullName) {return actorRepository.findByFullName(fullName);}

    @Transactional(rollbackFor = InsideResourceNotFoundException.class)
    public Actor edit(Long id, Actor requestActor) {
        try {
            return actorRepository.findById(id)
                    .map(actor -> {
                        actor.setFullName(requestActor.getFullName());
                        actor.setBirthday(requestActor.getBirthday());
                        actor.setMovies(requestActor.getMovies());
                        saveMovies(actor);
                        return actorRepository.save(actor);
                    }).orElseThrow(() -> new ResourceNotFoundException("Actor not found with id " + id));
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    public ResponseEntity<?> delete(Long id) {
        try {
            return actorRepository.findById(id)
                    .map(actor -> {
                        actorRepository.delete(actor);
                        return ResponseEntity.ok().build();
                    }).orElseThrow(() -> new ResourceNotFoundException("Actor not found with id " + id));
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    protected void saveMovies(Actor actor) {
        if (actor.getMovies()!=null)
            try {
                for (Movie movie:actor.getMovies()) {
                    Movie existingMovie=movieRepository.findById(movie.getId()).orElseThrow(()->new InsideResourceNotFoundException("Movie not found with id : " +movie.getId()+" inside actor object. "));
                    BeanUtils.copyProperties(existingMovie,movie);
                    movie.addActor(actor);
                    movieRepository.save(movie);
                }
            }
            catch (InsideResourceNotFoundException e) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, e.getMessage(), e);
            }
    }

}