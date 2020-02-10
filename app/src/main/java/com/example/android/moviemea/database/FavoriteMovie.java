package com.example.android.moviemea.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "favorite_movie")
public class FavoriteMovie {

    @PrimaryKey
    private Integer id;
    private String title;
    private String overview;
    @ColumnInfo(name = "release_date")
    private String releaseDate;
    @ColumnInfo(name = "poster_path")
    private String posterPath;      // acts also as relative local image file path
    @ColumnInfo(name = "vote_average")
    private Double voteAverage;

    public FavoriteMovie(Integer id, String title, String overview, String releaseDate, String posterPath, Double voteAverage) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.voteAverage = voteAverage;
    }

    @Ignore
    public FavoriteMovie(FavoriteMovie favoriteMovie) {
        this.id = favoriteMovie.getId();
        this.title = favoriteMovie.getTitle();
        this.overview = favoriteMovie.getOverview();
        this.releaseDate = favoriteMovie.getReleaseDate();
        this.posterPath = favoriteMovie.getPosterPath();
        this.voteAverage = favoriteMovie.getVoteAverage();
    }

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

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }


    @Override
    public String toString() {
        return "FavoriteMovie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", overview='" + overview + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", voteAverage=" + voteAverage +
                '}';
    }
}
