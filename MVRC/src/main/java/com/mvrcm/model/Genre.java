package com.mvrcm.model;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@Entity
@Table(name = "genres")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genre_generator")
    @SequenceGenerator(name="genre_generator", sequenceName = "genre_seq")
    private Long id;

    @Column(name = "title" ,nullable = false)
    private String title;

    @ManyToMany(mappedBy = "genres",fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Movie> movies;

    public Genre() {}

    public Genre(String title) {
        this.title = title;
    }

    public Genre(String title, Set<Movie> movies) {
        this.title = title;
        this.movies = movies;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title=title;
    }

    public Set<Movie> getMovies() {
        return movies;
    }

    public void setMovies(Set<Movie> movies) {
        this.movies = movies;
    }


}
