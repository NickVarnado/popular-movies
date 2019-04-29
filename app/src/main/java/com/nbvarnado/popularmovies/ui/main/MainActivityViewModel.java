package com.nbvarnado.popularmovies.ui.main;

import com.nbvarnado.popularmovies.data.MovieRepository;
import com.nbvarnado.popularmovies.data.database.movies.Movie;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {

    private final MovieRepository mRepository;
    private MutableLiveData<List<Movie>> mMovies;
    private String mSort;

    MainActivityViewModel(MovieRepository repository, String sort) {
        mRepository = repository;
        mSort = sort;
    }

    public LiveData<List<Movie>> getMovies() {
        if (mMovies == null) {
            mMovies = mRepository.getMovies(mSort);
        }
        return mMovies;
    }

    void setmSort(String sort) {
        this.mSort = sort;
        mMovies = mRepository.getMovies(mSort);
    }

}
