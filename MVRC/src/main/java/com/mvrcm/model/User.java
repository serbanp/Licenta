package com.mvrcm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Set;

@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@Entity
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username" ,nullable = false)
    private String username;

    @JsonIgnore
    @Column(name = "password" ,nullable = false)
    private String password;


    @Column(name = "email" ,nullable = false)
    private String email;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<UserMovieRating> userMovieRatingSet;

    public User(String username, String password, String email) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, String email, Set<UserMovieRating> userMovieRatingSet) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.userMovieRatingSet = userMovieRatingSet;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Set<UserMovieRating> getUserMovieRatingSet() {
        return userMovieRatingSet;
    }

    public void setUserMovieRatingSet(Set<UserMovieRating> userMovieRatingSet) {
        this.userMovieRatingSet = userMovieRatingSet;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}