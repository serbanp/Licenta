package com.mvrcm.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mvrcm.model.Utils.Persoana;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name="directors")
@Entity
public class Director extends Persoana {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "director_generator")
    @SequenceGenerator(name="director_generator", sequenceName = "director_seq")
    private Long id;

    @OneToMany(mappedBy = "director",orphanRemoval = true)
    @JsonBackReference
    private Set<Movie> movies;

    public Director(String fullName) {
        super(fullName);
    }

    public Director(String fullName,Date birthday) {
        super(fullName,birthday);
    }

    public Director(String fullName, Date birthday, String bio) {
        super(fullName, birthday, bio);
    }

    public Director() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Movie> getMovies() {
        return movies;
    }

    public void setMovies(Set<Movie> movies) {
        this.movies = movies;
    }

    public void addMovie(Movie movie) {
        this.movies.add(movie);
    }

    public void removeMovie(Movie movie) {
        this.movies.remove(movie);
    }



}
