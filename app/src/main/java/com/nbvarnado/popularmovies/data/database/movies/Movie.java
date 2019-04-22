package com.nbvarnado.popularmovies.data.database.movies;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "movie_table")
public class Movie implements Parcelable {

    @PrimaryKey
    private int id;

    @SerializedName("vote_average")
    private double voteAverage;

    private String title;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("backdrop_path")
    private String backdropPath;

    private String overview;

    @SerializedName("release_date")
    private String releaseDate;

    private int isFavorite;

    private String sortBy;

    protected Movie(Parcel in) {
        id = in.readInt();
        voteAverage = in.readDouble();
        title = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        isFavorite = in.readByte();
        sortBy = in.readString();
    }

    @Ignore
    public Movie() {

    }

    public Movie(int id,
                 double voteAverage,
                 String title,
                 String posterPath,
                 String backdropPath,
                 String overview,
                 String releaseDate,
                 int isFavorite,
                 String sortBy) {
        this.id = id;
        this.voteAverage = voteAverage;
        this.title = title;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.isFavorite = isFavorite;
        this.sortBy = sortBy;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeDouble(voteAverage);
        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeInt(isFavorite);
        dest.writeString(sortBy);
    }

    public int getId() {
        return this.id;
    }

    public double getVoteAverage() {
        return this.voteAverage;
    }

    public void setVoteAverage(double voteAverage) { this.voteAverage = voteAverage; }

    public String getTitle() {
        return this.title;
    }

    public String getBackdropPath() {
        return this.backdropPath;
    }

    public String getOverview() {
        return this.overview;
    }

    public void setOverview(String overview) { this.overview = overview; }

    public String getReleaseDate() {
        return this.releaseDate;
    }

    public int getIsFavorite() { return this.isFavorite; }

    public String getSortBy() { return this.sortBy; }

    public void setSortBy(String sortBy) { this.sortBy = sortBy; }

    public String getPosterPath() {
        return this.posterPath;
    }
}
