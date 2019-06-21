package com.mvrcm.model;


import com.fasterxml.jackson.annotation.*;
import com.mvrcm.model.Utils.AuditModel;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
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

    @Column(name="bio",length=100000)
    private String bio;

    @Column(name="img_src")
    private String imgSrc;

    @Column(name="release_date")
    private String releaseDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name= "movies_tags",joinColumns = @JoinColumn(name = "movie_id"),inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags=new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name= "movies_genres",joinColumns = @JoinColumn(name = "movie_id"),inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres=new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name= "movies_actors",joinColumns = @JoinColumn(name = "movie_id"),inverseJoinColumns = @JoinColumn(name = "actor_id"))
    private Set<Actor> actors=new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="director_id",nullable = false)
    private Director director;

    @JsonIgnore
    @OneToMany(mappedBy = "movie",fetch = FetchType.EAGER)
    private Set<UserMovieRating> userMovieRatings;

    @Column(name="avg_rating")
    private Double averageRating;

    public Movie() {}

    public Movie(String title, Set<Genre> genres) {
        this.title = title;
        this.genres = genres;
    }

    @BatchSize(size=100)
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

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void addActor(Actor actor) {
        this.actors.add(actor);
    }

    public void removeActor(Actor actor) {this.actors.remove(actor);}

    public void addGenre(Genre genre) {this.genres.add(genre);}

    public void removeGenre(Genre genre) {this.genres.remove(genre);}

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public void addTag(Tag tag) {this.tags.add(tag);}

    public void removeTag(Tag tag) {this.tags.remove(tag);}

    public void setAverageRating(Double avgRating) {
        DecimalFormat df = new DecimalFormat("#.#");
        df.setRoundingMode(RoundingMode.FLOOR);
        this.averageRating=Double.valueOf(df.format(avgRating));
    }

    public Double getAverageRating() {
        return averageRating;
    }
}
