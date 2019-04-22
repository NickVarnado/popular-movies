package com.nbvarnado.popularmovies.ui.detail;

import com.nbvarnado.popularmovies.data.MovieRepository;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MovieDetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final MovieRepository mMovieReposiory;
    private final int mMovieId;

    public MovieDetailsViewModelFactory(MovieRepository repository, int movieId) {
        this.mMovieReposiory = repository;
        this.mMovieId = movieId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new MovieDetailsViewModel(mMovieReposiory, mMovieId);
    }

}
