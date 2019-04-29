package com.nbvarnado.popularmovies.data.network;

import android.content.Context;

import com.nbvarnado.popularmovies.AppExecutors;
import com.nbvarnado.popularmovies.BuildConfig;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDataSource {

    private static final String TAG = MovieDataSource.class.getSimpleName();

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

}
