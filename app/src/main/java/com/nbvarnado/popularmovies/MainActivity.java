package com.nbvarnado.popularmovies;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nbvarnado.popularmovies.model.Movie;
import com.nbvarnado.popularmovies.model.Page;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

enum SortType {
    POPULAR, RATINGS
}

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieClickListener {

    private final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessage;

    private SortType sortType;

    private static final String POPULAR = "popular";
    private static final String RATINGS = "top_rated";

    public static final String EXTRA_MOVIE = "com.nbvarnado.popularmovie.MOVIE";

    public static final String TITLE = "title";
    public static final String IMAGE = "image";
    public static final String RELEASE_DATE = "release_date";
    public static final String VOTE_AVERAGE = "vote_average";
    public static final String OVERVIEW = "overview";

    private static final String BASE_URL = "http://api.themoviedb.org/3/movie/";

    // TODO: Add API KEY here.
    private static final String API_KEY = "";

    private static final String API_KEY_PARAM = "api_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sortType = sortType == null ? SortType.POPULAR : sortType;
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.rv_movies);
        mLoadingIndicator = findViewById(R.id.pb_loading);
        mErrorMessage = findViewById(R.id.error_message);

        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);
        loadMovies();
    }

    @Override
    public void onMovieClick(Movie movie) {
        Intent intent = new Intent(this, MovieDetails.class);

        Bundle bundle = new Bundle();
        bundle.putString(TITLE, movie.getTitle());
        bundle.putString(IMAGE, movie.getImageUrl());
        bundle.putString(RELEASE_DATE, movie.getReleaseDate());
        bundle.putString(VOTE_AVERAGE, movie.getVoteAverage());
        bundle.putString(OVERVIEW, movie.getOverview());

        intent.putExtra(EXTRA_MOVIE, bundle);
        startActivity(intent);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Only display the menu item for the sort type that is not currently selected.
        int idOfItemToHide = sortType == SortType.POPULAR ? R.id.sort_popular : R.id.sort_rating;
        int idOfItemToShow = sortType == SortType.POPULAR ? R.id.sort_rating : R.id.sort_popular;
        MenuItem itemToHide = menu.findItem(idOfItemToHide);
        MenuItem itemToShow = menu.findItem(idOfItemToShow);
        itemToHide.setVisible(false);
        itemToShow.setVisible(true);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_popular:
                sortType = SortType.POPULAR;
                loadMovies();
                return true;
            case R.id.sort_rating:
                sortType = SortType.RATINGS;
                loadMovies();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Uses Retrofit to get data from themoviedb API and set the movie data on the MovieAdapter.
     */
    private void loadMovies() {
        showLoading();
        String sort = sortType == SortType.POPULAR ? POPULAR : RATINGS;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieDbService service = retrofit.create(MovieDbService.class);

        Call<Page> call = service.getResults(sort, API_KEY);
        call.enqueue(new Callback<Page>() {
            @Override
            public void onResponse(@NonNull Call<Page> call, @NonNull Response<Page> response) {
                Page page = response.body();
                if (page != null) {
                    List<Movie> movies = page.getResults();
                    mMovieAdapter.setMovieData(movies);
                    mMovieAdapter.notifyDataSetChanged();
                    showMovies();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Page> call, @NonNull Throwable t) {
                Log.e(TAG, t.getMessage());
                showError();
            }
        });
    }

    /**
     * Show the ProgressBar while loading the data.
     */
    private void showLoading() {
        mErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    /**
     * Show the RecyclerView when the data is loaded.
     */
    private void showMovies() {
        mErrorMessage.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * Show the error message onFailure.
     */
    private void showError() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    /**
     * Interface for querying themoviedb API.
     */
    public interface MovieDbService {
        @GET("{sort}/")
        Call<Page> getResults(@Path("sort") String sort, @Query(API_KEY_PARAM) String apiKey);
    }

}
