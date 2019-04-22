package com.nbvarnado.popularmovies.data.database;

import android.content.Context;
import android.util.Log;

import com.nbvarnado.popularmovies.data.database.movies.Movie;
import com.nbvarnado.popularmovies.data.database.movies.MovieDao;
import com.nbvarnado.popularmovies.data.database.reviews.Review;
import com.nbvarnado.popularmovies.data.database.reviews.ReviewDao;
import com.nbvarnado.popularmovies.data.database.trailers.Trailer;
import com.nbvarnado.popularmovies.data.database.trailers.TrailerDao;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Movie.class, Review.class, Trailer.class}, version = 1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    private static final String TAG = MovieDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DB_NAME = "movies";
    private static MovieDatabase sInstance;

    public static MovieDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context,
                        MovieDatabase.class, MovieDatabase.DB_NAME)
                        .build();
            }
        }
        Log.d(TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract MovieDao movieDao();

    public abstract ReviewDao reviewDao();

    public abstract TrailerDao trailerDao();
}
