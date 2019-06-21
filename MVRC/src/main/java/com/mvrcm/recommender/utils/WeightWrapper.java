package com.mvrcm.recommender.utils;

public class WeightWrapper {
    private float rating;
    private int noOfMovies;
    private float weight;

    public WeightWrapper() {
    }

    public WeightWrapper(float rating, int noOfMovies) {
        this.rating = rating;
        this.noOfMovies = noOfMovies;
    }

    public void setWeight(float weight) {
        this.weight=weight;
    }

    public float getRating() {
        return rating;
    }

    public float getWeight() {
        return this.weight;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getNoOfMovies() {
        return noOfMovies;
    }

    public void addRating(float rating) {
        this.rating+=rating;
    }

    public void incrementNoOfMOvies() {
        this.noOfMovies++;
    }

    public void setNoOfMovies(int noOfMovies) {
        this.noOfMovies = noOfMovies;
    }


}
