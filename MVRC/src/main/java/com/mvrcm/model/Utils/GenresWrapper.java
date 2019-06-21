package com.mvrcm.model.Utils;

import com.mvrcm.model.Genre;

import java.util.Set;

public class GenresWrapper {
    private Set<Genre> genres;

    public GenresWrapper() {}

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }
}
