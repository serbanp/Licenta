package com.mvrcm.controller;


import com.mvrcm.model.Director;
import com.mvrcm.repository.DirectorRepository;
import com.mvrcm.service.DirectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class DirectorController {
    @Autowired
    private DirectorService directorService;


    @GetMapping("/directors")
    public List<Director> getDirectors() {return directorService.getAll();}

    @GetMapping("/directors/{id}")
    public Director getDirectorById(@PathVariable Long id) { return directorService.getById(id);}

    @PostMapping("/directors")
    public Director createDirector(@Valid @RequestBody Director director) {return directorService.create(director);}

    @PutMapping("/directors/{id}")
    public Director editDirector(@PathVariable Long id,@Valid @RequestBody Director director) {
        return directorService.edit(id,director);
    }

    @DeleteMapping("/directors/{id}")
    public ResponseEntity<?> deleteDirector(@PathVariable Long id) {
        return directorService.delete(id);
    }
}
