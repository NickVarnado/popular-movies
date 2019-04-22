package com.nbvarnado.popularmovies.ui.main;

import com.nbvarnado.popularmovies.data.MovieRepository;
import com.nbvarnado.popularmovies.data.database.movies.Movie;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {

    private final MovieRepository mRepository;
    private final LiveData<List<Movie>> mMovies;

    public MainActivityViewModel(MovieRepository repository, String sort) {
        mRepository = repository;
        mMovies = repository.getMovies(sort);
    }

    public LiveData<List<Movie>> getMovies(String sort) {
        return mMovies;
    }

}
