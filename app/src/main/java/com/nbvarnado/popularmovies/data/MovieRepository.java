package com.nbvarnado.popularmovies.data;

import android.content.Context;
import android.util.Log;

import com.nbvarnado.popularmovies.AppExecutors;
import com.nbvarnado.popularmovies.BuildConfig;
import com.nbvarnado.popularmovies.data.database.movies.Movie;
import com.nbvarnado.popularmovies.data.database.movies.MovieDao;
import com.nbvarnado.popularmovies.data.database.MovieDatabase;
import com.nbvarnado.popularmovies.data.database.movies.MoviesPage;
import com.nbvarnado.popularmovies.data.database.reviews.Review;
import com.nbvarnado.popularmovies.data.database.reviews.ReviewDao;
import com.nbvarnado.popularmovies.data.database.reviews.ReviewsPage;
import com.nbvarnado.popularmovies.data.database.trailers.Trailer;
import com.nbvarnado.popularmovies.data.database.trailers.TrailerDao;
import com.nbvarnado.popularmovies.data.database.trailers.TrailersPage;
import com.nbvarnado.popularmovies.data.network.MovieDataSource;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nick Varnado on 3/13/2019.
 */

public class MovieRepository {

    private static final String TAG = MovieRepository.class.getSimpleName();

    private static final Object LOCK = new Object();
    private static MovieRepository sInstance;
    private static boolean mIsInitialized;
    private final MovieDao mMovieDao;
    private final ReviewDao mReviewDao;
    private final TrailerDao mTrailerDao;
    private final MovieDataSource mMovieDataSource;
    private final MovieDatabase mMovieDatabase;
    private final Context mContext;
    private final AppExecutors mAppExecutors;

    // TODO: Add API KEY here.
    private static final String API_KEY = BuildConfig.ApiKey;

    private MovieRepository(Context context,
                            MovieDatabase database,
                            MovieDataSource dataSource,
                            AppExecutors executors) {

        mContext = context;
        mMovieDatabase = database;
        mMovieDataSource = dataSource;
        mMovieDao = mMovieDatabase.movieDao();
        mReviewDao = mMovieDatabase.reviewDao();
        mTrailerDao = mMovieDatabase.trailerDao();
        mAppExecutors = executors;

    }

    public synchronized static MovieRepository getInstance(Context context,
                                                           MovieDatabase database,
                                                           MovieDataSource dataSource,
                                                           AppExecutors executors) {

        Log.d(TAG, "Getting repositiory instance");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new MovieRepository(context, database, dataSource, executors);
                Log.d(TAG, "Repository created.");
            }
        }
        return sInstance;
    }

    /**
     * Uses Retrofit to get movie data from themoviedb API and save it to the database.
     */
    private void loadMovies(String sort) {

        Call<MoviesPage> call = mMovieDataSource.getService(mContext, mAppExecutors).getMovies(sort, API_KEY);
        call.enqueue(new Callback<MoviesPage>() {
            @Override
            public void onResponse(@NonNull Call<MoviesPage> call, @NonNull Response<MoviesPage> response) {
                MoviesPage page = response.body();
                if (page != null) {
                    List<Movie> movies = page.getMovies();
                    mAppExecutors.diskIO().execute(() -> {
                        for(Movie movie : movies) {
                            movie.setSortBy(sort);
                            mMovieDatabase.movieDao().insertMovie(movie);
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<MoviesPage> call, @NonNull Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });

    }

    /**
     * Uses Retrofit to get review data from themoviedb API and save it to the database.
     */
    private void loadReviews(String movieId) {

        Call<ReviewsPage> call = mMovieDataSource.getService(mContext, mAppExecutors).getReviews(movieId, API_KEY);
        call.enqueue(new Callback<ReviewsPage>() {
            @Override
            public void onResponse(@NonNull Call<ReviewsPage> call, @NonNull Response<ReviewsPage> response) {
                ReviewsPage page = response.body();
                if (page != null) {
                    List<Review> reviews = page.getReviews();
                    mAppExecutors.diskIO().execute(() -> {
                        for (Review review : reviews) {
                            review.setMovieId(Integer.parseInt(movieId));
                            mMovieDatabase.reviewDao().insertReview(review);
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<ReviewsPage> call, @NonNull Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });

    }

    /**
     * Uses Retrofit to get trailer data from themoviedb API and save it to the database.
     */
    private void loadTrailers(String movieId) {

        Call<TrailersPage> call = mMovieDataSource.getService(mContext, mAppExecutors).getTrailers(movieId, API_KEY);
        call.enqueue(new Callback<TrailersPage>() {
            @Override
            public void onResponse(@NonNull Call<TrailersPage> call, @NonNull Response<TrailersPage> response) {
                TrailersPage page = response.body();
                if (page != null) {
                    List<Trailer> trailers = page.getTrailers();
                    mAppExecutors.diskIO().execute(() -> {
                        for (Trailer trailer : trailers) {
                            trailer.setMovieId(Integer.parseInt(movieId));
                            mMovieDatabase.trailerDao().insertTrailer(trailer);
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<TrailersPage> call, @NonNull Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });

    }

    public void toggleFavorite(int isFav, int movieId) {
        mMovieDatabase.movieDao().toggleFavorite(isFav, movieId);
    }

    public LiveData<Movie> getMovie(int id) {
        LiveData<Movie> movie = mMovieDao.getMovieById(id);
        return movie;
    }

    public LiveData<List<Movie>> getMovies(String sort) {
        LiveData<List<Movie>> movies = mMovieDao.getMovies(sort);
        if (movies.getValue() == null || movies.getValue().size() == 0) {
            loadMovies(sort);
        }
        return movies;
    }

    public LiveData<List<Review>> getReviews(int id) {
        LiveData<List<Review>> reviews = mReviewDao.getReviews(id);
        if (reviews.getValue() == null || reviews.getValue().size() == 0) {
            loadReviews(String.valueOf(id));
        }
        return reviews;
    }

    public LiveData<List<Trailer>> getTrailers(int id) {
        LiveData<List<Trailer>> trailers = mTrailerDao.getTrailers(id);
        if (trailers.getValue() == null || trailers.getValue().size() == 0) {
            loadTrailers(String.valueOf(id));
        }
        return trailers;
    }

}
