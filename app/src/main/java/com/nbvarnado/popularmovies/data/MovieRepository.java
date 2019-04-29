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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Nick Varnado on 3/13/2019.
 */

public class MovieRepository {

    private static final String TAG = MovieRepository.class.getSimpleName();

    private static final Object LOCK = new Object();
    private static MovieRepository sInstance;
    private final MovieDao mMovieDao;
    private final ReviewDao mReviewDao;
    private final TrailerDao mTrailerDao;
    private final MovieDataSource mMovieDataSource;
    private final MovieDatabase mMovieDatabase;
    private final Context mContext;
    private final AppExecutors mAppExecutors;
    private MutableLiveData<List<Movie>> movies;

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
        movies = new MutableLiveData<>();
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
    private List<Movie> loadMovies(String sort) {

        Call<MoviesPage> call = mMovieDataSource.getService(mContext, mAppExecutors).getMovies(sort, API_KEY);
        List<Movie> movies = new ArrayList<>();
        Response<MoviesPage> moviePageResponse = null;
        try {
            moviePageResponse = call.execute();
            if (moviePageResponse.body() != null) {
                movies = moviePageResponse.body().getMovies();
                for(Movie movie : movies) {
                    movie.setSortBy(sort);
                    mMovieDatabase.movieDao().insertMovie(movie);
                }
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return movies;
    }

    /**
     * Uses Retrofit to get review data from themoviedb API and save it to the database.
     */
    private List<Review> loadReviews(String movieId) {

        Call<ReviewsPage> call = mMovieDataSource.getService(mContext, mAppExecutors).getReviews(movieId, API_KEY);
        List<Review> reviews = new ArrayList<>();
        Response<ReviewsPage> reviewPage = null;
        try {
            reviewPage = call.execute();
            if (reviewPage.body() != null) {
                reviews = reviewPage.body().getReviews();
                for (Review review : reviews) {
                    review.setMovieId(Integer.parseInt(movieId));
                    mMovieDatabase.reviewDao().insertReview(review);
                }
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return reviews;
    }

    /**
     * Uses Retrofit to get trailer data from themoviedb API and save it to the database.
     */
    private List<Trailer> loadTrailers(String movieId) {

        Call<TrailersPage> call = mMovieDataSource.getService(mContext, mAppExecutors).getTrailers(movieId, API_KEY);
        List<Trailer> trailers = new ArrayList<>();
        Response<TrailersPage> trailersPageResponse = null;
        try {
            trailersPageResponse = call.execute();
            if (trailersPageResponse.body() != null) {
                trailers = trailersPageResponse.body().getTrailers();
                for (Trailer trailer : trailers) {
                    trailer.setMovieId(Integer.parseInt(movieId));
                    mMovieDatabase.trailerDao().insertTrailer(trailer);
                }
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return trailers;

    }

    /**
     * Set's isFavorite to 1 or 0 for the given movieId in the db.
     * @param isFav
     * @param movieId
     */
    public void toggleFavorite(int isFav, int movieId) {
        mMovieDatabase.movieDao().toggleFavorite(isFav, movieId);
    }

    /**
     * Get's the LiveData object for the movie with the given id.
     * @param id
     * @return
     */
    public LiveData<Movie> getMovie(int id) {
        LiveData<Movie> movie = mMovieDao.getMovieById(id);
        return movie;
    }

    /**
     * Get's the LiveDate object for the movies. If sort is favorites, the favorite movies are queried
     * from the database. Otherwise the movies are queried from the database first. If nothing is
     * returned from the database a network call is executed.
     * @param sort Sort parameter.
     * @return
     */
    public MutableLiveData<List<Movie>> getMovies(String sort) {
        if (sort.equals("favorite")) {
            AppExecutors.getInstance().diskIO().execute(() -> {
                List<Movie> favoriteMovies = mMovieDao.getFavoriteMovies();
                movies.postValue(favoriteMovies);
            });
        } else {
            AppExecutors.getInstance().diskIO().execute(() -> {
                List<Movie> moviesFromDb = mMovieDao.getMovies(sort);
                if (moviesFromDb == null || moviesFromDb.size() == 0) {
                    AppExecutors.getInstance().networkIO().execute(() -> {
                        List<Movie> moviesFromNetwork = loadMovies(sort);
                        movies.postValue(moviesFromNetwork);
                    });
                } else {
                    movies.postValue(moviesFromDb);
                }
            });
        }
        return movies;
    }

    /**
     * Get's the LiveDate object for the reviews. The reviews are queried from the database first. If nothing is
     * returned from the database a network call is executed.
     * @param id of the movie to get the reviews for.
     * @return
     */
    public LiveData<List<Review>> getReviews(int id) {
        final MutableLiveData<List<Review>> reviews = new MutableLiveData<>();
        AppExecutors.getInstance().diskIO().execute(() -> {
            List<Review> reviewsFromDb = mReviewDao.getReviews(id);
            if (reviewsFromDb == null || reviewsFromDb.size() == 0) {
                AppExecutors.getInstance().networkIO().execute(() -> {
                    List<Review> reviewsFromNetwork = loadReviews(String.valueOf(id));
                    reviews.postValue(reviewsFromNetwork);
                });
            } else {
                reviews.postValue(reviewsFromDb);
            }
        });
        return reviews;
    }

    /**
     * Get's the LiveDate object for the trailers. The trailers are queried from the database first. If nothing is
     * returned from the database a network call is executed.
     * @param id of the movie to get the reviews for.
     * @return
     */
    public LiveData<List<Trailer>> getTrailers(int id) {
        final MutableLiveData<List<Trailer>> trailers = new MutableLiveData<>();
        AppExecutors.getInstance().diskIO().execute(() -> {
            List<Trailer> trailersFromDb = mTrailerDao.getTrailers(id);
            if (trailersFromDb == null || trailersFromDb.size() == 0) {
                AppExecutors.getInstance().networkIO().execute(() -> {
                    List<Trailer> trailersFromNetwork = loadTrailers(String.valueOf(id));
                    trailers.postValue(trailersFromNetwork);
                });
            } else {
                trailers.postValue(trailersFromDb);
            }
        });
        return trailers;
    }

}
