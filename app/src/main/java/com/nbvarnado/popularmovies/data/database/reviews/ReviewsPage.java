package com.nbvarnado.popularmovies.data.database.reviews;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewsPage implements Parcelable {

    private int id;

    private int page;

    @SerializedName("results")
    private List<Review> reviews;

    @SerializedName("total_pages")
    private int totalPages;

    @SerializedName("total_results")
    private int totalResults;

    protected ReviewsPage(Parcel in) {
        this.id = in.readInt();
        this.page = in.readInt();
        in.readList(this.reviews, Review.class.getClassLoader());
        this.totalPages = in.readInt();
        this.totalResults = in.readInt();
    }

    public static final Creator<ReviewsPage> CREATOR = new Creator<ReviewsPage>() {
        @Override
        public ReviewsPage createFromParcel(Parcel in) {
            return new ReviewsPage(in);
        }

        @Override
        public ReviewsPage[] newArray(int size) {
            return new ReviewsPage[size];
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
        dest.writeList(reviews);
        dest.writeInt(totalPages);
        dest.writeInt(totalResults);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }
}
