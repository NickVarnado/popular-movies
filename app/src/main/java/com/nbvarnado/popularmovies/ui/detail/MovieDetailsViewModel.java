package com.nbvarnado.popularmovies.ui.detail;

import com.nbvarnado.popularmovies.data.MovieRepository;
import com.nbvarnado.popularmovies.data.database.movies.Movie;
import com.nbvarnado.popularmovies.data.database.reviews.Review;
import com.nbvarnado.popularmovies.data.database.trailers.Trailer;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class MovieDetailsViewModel extends ViewModel {

    private LiveData<Movie> mMovie;
    private LiveData<List<Trailer>> mTrailers;
    private LiveData<List<Review>> mReviews;

    MovieDetailsViewModel(MovieRepository repository, int id) {
        mMovie = repository.getMovie(id);
        mTrailers = repository.getTrailers(id);
        mReviews = repository.getReviews(id);
    }

    public LiveData<Movie> getMovie() {
        return mMovie;
    }

    public LiveData<List<Trailer>> getTrailers() {
        return mTrailers;
    }

    public LiveData<List<Review>> getReviews() {
        return mReviews;
    }
}
