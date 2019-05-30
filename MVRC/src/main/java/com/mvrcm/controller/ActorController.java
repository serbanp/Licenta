package com.mvrcm.controller;

import com.mvrcm.model.Actor;
import com.mvrcm.model.Movie;
import com.mvrcm.service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
public class ActorController {

    @Autowired
    private ActorService actorService;

    @GetMapping("/actors")
    public List<Actor> getActors() {return actorService.getAll();}

    @GetMapping("/actors/{id}")
    public Actor getActorById(@PathVariable Long id) {
        return actorService.getById(id);
    }

    @GetMapping("/actors/{id}/movies")
    public Set<Movie> getActorMovies(@PathVariable Long id ) {return actorService.getById(id).getMovies();}

    @PostMapping("/actors")
    public Actor createActor(@Valid @RequestBody Actor actor) {
        return actorService.create(actor);
    }

    @PutMapping("/actors/{id}")
    public Actor editActor(@PathVariable Long id,@Valid @RequestBody Actor actor) {
        return actorService.edit(id,actor);
    }

    @DeleteMapping("/actors/{id}")
    public ResponseEntity<?> deleteActor(@PathVariable Long id) {
        return actorService.delete(id);
    }
}
