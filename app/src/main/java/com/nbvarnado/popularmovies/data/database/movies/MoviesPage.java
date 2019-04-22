package com.nbvarnado.popularmovies.data.database.movies;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviesPage implements Parcelable {

    private int id;
    private int page;

    @SerializedName("results")
    private List<Movie> movies;

    @SerializedName("total_pages")
    private int totalPages;

    @SerializedName("total_results")
    private int totalResults;

    protected MoviesPage(Parcel in) {
        id = in.readInt();
        page = in.readInt();
        movies = in.createTypedArrayList(Movie.CREATOR);
        totalPages = in.readInt();
        totalResults = in.readInt();
    }

    public static final Creator<MoviesPage> CREATOR = new Creator<MoviesPage>() {
        @Override
        public MoviesPage createFromParcel(Parcel in) {
            return new MoviesPage(in);
        }

        @Override
        public MoviesPage[] newArray(int size) {
            return new MoviesPage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(page);
        dest.writeTypedList(movies);
        dest.writeInt(totalPages);
        dest.writeInt(totalResults);
    }

    public List<Movie> getMovies() {
        return this.movies;
    }
}
