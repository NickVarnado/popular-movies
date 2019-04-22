package com.nbvarnado.popularmovies.data.database.trailers;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailersPage implements Parcelable {

    private int id;

    @SerializedName("results")
    private List<Trailer> trailers;

    protected TrailersPage(Parcel in) {
        id = in.readInt();
        trailers = in.createTypedArrayList(Trailer.CREATOR);
    }

    public static final Creator<TrailersPage> CREATOR = new Creator<TrailersPage>() {
        @Override
        public TrailersPage createFromParcel(Parcel in) {
            return new TrailersPage(in);
        }

        @Override
        public TrailersPage[] newArray(int size) {
            return new TrailersPage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeTypedList(trailers);
    }

    public List<Trailer> getTrailers() {
        return this.trailers;
    }
}
