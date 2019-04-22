package com.nbvarnado.popularmovies.data.network;

import android.content.Context;
import android.util.Log;

import com.nbvarnado.popularmovies.AppExecutors;
import com.nbvarnado.popularmovies.BuildConfig;
import com.nbvarnado.popularmovies.R;
import com.nbvarnado.popularmovies.RequestCallback;
import com.nbvarnado.popularmovies.SortType;
import com.nbvarnado.popularmovies.data.database.movies.Movie;
import com.nbvarnado.popularmovies.data.database.movies.MoviesPage;
import com.nbvarnado.popularmovies.ui.main.MovieAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDataSource {

    private static final String TAG = MovieDataSource.class.getSimpleName();

    // TODO: Add API KEY here.
    private static final String API_KEY = BuildConfig.ApiKey;

    private static MovieDataSource sInstance;
    private MovieDbService service;

    private Context mContext;
    private AppExecutors mExecutors;

    // API URL data
    private static final String BASE_URL = "http://api.themoviedb.org/3/movie/";

    private MovieDataSource(Context context, AppExecutors executors) {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        service = retrofit.create(MovieDbService.class);
        mContext = context;
        mExecutors = executors;
    }

    public static synchronized MovieDataSource getInstance(Context context, AppExecutors executors) {
        if (sInstance == null) {
            sInstance = new MovieDataSource(context, executors);
        }
        return sInstance;
    }


    public MovieDbService getService(Context context, AppExecutors executors) {
        if (service == null) {
            getInstance(context, executors);
        }
        return service;
    }

    /**
     * Uses Retrofit to get data from themoviedb API and set the movie data on the MovieAdapter.
     */
    public void loadMovies(Context context, SortType sortType) {

        mExecutors.networkIO().execute(() -> {
            final String POPULAR = context.getResources().getString(R.string.popular);
            final String RATINGS = context.getResources().getString(R.string.top_rated);
            String sort = sortType == SortType.POPULAR ? POPULAR : RATINGS;

            Call<MoviesPage> call = service.getMovies(sort, API_KEY);
            call.enqueue(new Callback<MoviesPage>() {
                @Override
                public void onResponse(@NonNull Call<MoviesPage> call, @NonNull Response<MoviesPage> response) {
                    MoviesPage page = response.body();
                    if (page != null) {
                        List<Movie> movies = page.getMovies();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MoviesPage> call, @NonNull Throwable t) {
                    Log.e(TAG, t.getMessage());
                }
            });
        });

    }
}
