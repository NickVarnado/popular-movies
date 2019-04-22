package com.nbvarnado.popularmovies.ui.main;

import com.nbvarnado.popularmovies.data.MovieRepository;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final MovieRepository mMovieRepository;
    private final String mSort;

    public MainViewModelFactory(MovieRepository movieRepository, String sort) {
        this.mMovieRepository = movieRepository;
        mSort = sort;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new MainActivityViewModel(mMovieRepository, mSort);
    }
}
