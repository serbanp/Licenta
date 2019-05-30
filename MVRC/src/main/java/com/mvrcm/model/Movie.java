package com.mvrcm.model;


import com.fasterxml.jackson.annotation.*;
import com.mvrcm.model.Utils.AuditModel;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movie_generator")
    @SequenceGenerator(name="movie_generator", sequenceName = "movie_seq")
    private Long id;

    @Column(name = "title" ,nullable = false)
    private String title;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name= "movies_genres",joinColumns = @JoinColumn(name = "movie_id"),inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres=new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name= "movies_actors",joinColumns = @JoinColumn(name = "movie_id"),inverseJoinColumns = @JoinColumn(name = "actor_id"))
    private Set<Actor> actors=new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="director_id",nullable = false)
    private Director director;

    @JsonIgnore
    @OneToMany(mappedBy = "movie")
    private Set<UserMovieRating> userMovieRatings;

    public Movie() {}

    public Movie(String title, Set<Genre> genres) {
        this.title = title;
        this.genres = genres;
    }

    public Set<Actor> getActors() {
        return actors;
    }

    public void setActors(Set<Actor> actors) {
        this.actors = actors;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public Director getDirector() {
        return director;
    }

    public void setDirector(Director director) {
        this.director = director;
    }

    public void addActor(Actor actor) {
        this.actors.add(actor);
    }

    public void removeActor(Actor actor) {this.actors.remove(actor);}

    public void addGenre(Genre genre) {this.genres.add(genre);}

    public void removeGenre(Genre genre) {this.genres.remove(genre);}
}
