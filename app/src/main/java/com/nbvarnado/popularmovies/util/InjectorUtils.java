package com.nbvarnado.popularmovies.util;

import android.content.Context;

import com.nbvarnado.popularmovies.AppExecutors;
import com.nbvarnado.popularmovies.data.MovieRepository;
import com.nbvarnado.popularmovies.data.database.MovieDatabase;
import com.nbvarnado.popularmovies.data.network.MovieDataSource;
import com.nbvarnado.popularmovies.ui.detail.MovieDetailsViewModelFactory;
import com.nbvarnado.popularmovies.ui.main.MainViewModelFactory;

public class InjectorUtils {

    public static MovieRepository provideRepository(Context context) {
        MovieDatabase database = MovieDatabase.getInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        MovieDataSource movieDataSource = MovieDataSource.getInstance(context.getApplicationContext(), executors);
        return MovieRepository.getInstance(context, database, movieDataSource, executors);
    }

    public static MainViewModelFactory provideMainViewModelFactory(Context context, String sort) {
        MovieRepository repository = provideRepository(context.getApplicationContext());
        return new MainViewModelFactory(repository, sort);
    }

    public static MovieDetailsViewModelFactory provideMovieDetailsViewModelFactory(Context context, int id) {
        MovieRepository repository = provideRepository(context.getApplicationContext());
        return new MovieDetailsViewModelFactory(repository, id);
    }
}
