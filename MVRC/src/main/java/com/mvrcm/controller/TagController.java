package com.mvrcm.controller;

import com.mvrcm.model.Genre;
import com.mvrcm.model.Tag;
import com.mvrcm.service.MovieService;
import com.mvrcm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/tags/{id}")
    public Tag getTagById(@PathVariable Long id) {return tagService.getById(id);}

    @GetMapping("/tags")
    public List<Tag> getTags() {
        return tagService.getAll();
    }

    @PostMapping("/tags")
    public Tag createTag(@Valid @RequestBody Tag tag) {
        return tagService.create(tag);
    }

    @DeleteMapping("/tags/{id}")
    public ResponseEntity<?> deleteTag(@PathVariable Long id) {
        return tagService.delete(id);
    }
}
