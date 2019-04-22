package com.nbvarnado.popularmovies.data.database.reviews;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "review_table")
public class Review implements Parcelable {

    @NonNull
    @PrimaryKey
    private String id;

    private int movieId;

    private String author;

    private String content;

    protected Review(Parcel in) {
        this.id = in.readString();
        this.movieId = in.readInt();
        this.author = in.readString();
        this.content = in.readString();
    }

    @Ignore
    public Review() {

    }

    public Review(String id, int movieId, String author, String content) {
        this.id = id;
        this.movieId = movieId;
        this.author = author;
        this.content = content;
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(movieId);
        dest.writeString(author);
        dest.writeString(content);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }
}
