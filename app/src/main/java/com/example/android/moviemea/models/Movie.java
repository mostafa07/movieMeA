package com.example.android.moviemea.models;

import java.util.ArrayList;


public class Movie {

    private Integer id;
    private String title;
    // private String originalTitle;
    // private String originalLanguage;
    private String overview;
    private String releaseDate;
    private String posterPath;
    private String backdropPath;
    private Double popularity;
    private Double voteAverage;
    // private Integer voteCount;
    // private Boolean adult;
    // private Boolean video;
    private ArrayList<Integer> genreIds;


    /* Constructors */

    public Movie() {
    }

    public Movie(Integer id, String title, String overview, String releaseDate, String posterPath,
                 String backdropPath, Double popularity, Double voteAverage, ArrayList<Integer> genreIds) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.popularity = popularity;
        this.voteAverage = voteAverage;
        this.genreIds = genreIds;
    }


    /* Getters and Setters */

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public ArrayList<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(ArrayList<Integer> genreIds) {
        this.genreIds = genreIds;
    }


    /* Other Methods */

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", overview='" + overview + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", backdropPath='" + backdropPath + '\'' +
                ", popularity=" + popularity +
                ", voteAverage=" + voteAverage +
                ", genreIds=" + genreIds +
                '}';
    }
}
