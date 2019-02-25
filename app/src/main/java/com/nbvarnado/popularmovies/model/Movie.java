package com.nbvarnado.popularmovies.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Nick Varnado on 2/19/2019.
 */

public class Movie {

    private String title;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("vote_average")
    private String voteAverage;
    private String overview;

    /**
     * Constructor
     */
    public Movie() {

    }

    /**
     * Constructor
     */
    public Movie(String title, String posterPath, String releaseDate, String voteAverage, String overview) {
        this.title = title;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.overview = overview;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    /**
     * Get's the image URL String.
     * @return The String for the image URL.
     */
    public String getImageUrl() {
        String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
        // Size can be one of the following. "w92", "w154", "w185", "w342", "w500", "w780", or "original".
        String IMAGE_SIZE = "w185";
        return IMAGE_BASE_URL + IMAGE_SIZE + this.posterPath;
    }
}
