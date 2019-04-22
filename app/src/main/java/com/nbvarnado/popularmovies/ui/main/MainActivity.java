package com.nbvarnado.popularmovies.ui.main;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nbvarnado.popularmovies.R;
import com.nbvarnado.popularmovies.SortType;
import com.nbvarnado.popularmovies.ui.detail.MovieDetailsActivity;
import com.nbvarnado.popularmovies.util.InjectorUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements MovieAdapter.MovieClickListener {

    private final String TAG = MainActivity.class.getSimpleName();

    // State keys
    private static final String LIFECYCLE_CALLBACK_SORT_KEY = "sort_type";

    // Intent Extra
    public static final String EXTRA_MOVIE_ID = "extra_movie_id";

    @BindView(R.id.rv_movies) RecyclerView mRecyclerView;
    @BindView(R.id.pb_loading) ProgressBar mLoadingIndicator;
    @BindView(R.id.tv_error_message) TextView mErrorMessage;

    private MovieAdapter mMovieAdapter;
    private MainActivityViewModel mMainActivityViewModel;
    private SortType sortType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        sortType = sortType == null ? SortType.POPULAR : sortType;

        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        showLoading();
        loadMovies(sortType);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        final String POPULAR = getResources().getString(R.string.popular);
        final String RATINGS = getResources().getString(R.string.top_rated);
        String sortTypeString = sortType == SortType.POPULAR ? POPULAR : RATINGS;
        outState.putString(LIFECYCLE_CALLBACK_SORT_KEY, sortTypeString);
    }

    @Override
    public void onMovieClick(int id) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra(EXTRA_MOVIE_ID, id);
        startActivity(intent);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Only display the menu item for the sort type that is not currently selected.
        int idOfItemToHide = sortType == SortType.POPULAR ? R.id.sort_popular : R.id.sort_rating;
        int idOfItemToShow = sortType == SortType.POPULAR ? R.id.sort_rating : R.id.sort_popular;
        MenuItem itemToHide = menu.findItem(idOfItemToHide);
        MenuItem itemToShow = menu.findItem(idOfItemToShow);
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
                showLoading();
                loadMovies(sortType);
                return true;
            case R.id.sort_rating:
                sortType = SortType.RATINGS;
                showLoading();
                loadMovies(sortType);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadMovies(SortType sortType) {
        String sort = getSort(sortType);
        MainViewModelFactory factory = InjectorUtils.provideMainViewModelFactory(this, sort);
        mMainActivityViewModel = ViewModelProviders.of (this, factory).get(MainActivityViewModel.class);

        mMainActivityViewModel.getMovies(sort).observe(this, movies -> {
            if (movies != null) {
                mMovieAdapter.setMovieData(movies);
                mMovieAdapter.notifyDataSetChanged();
                showMovies();
            } else {
                showError();
            }

        });
    }

    private String getSort(SortType sortType) {
        switch (sortType) {
            case POPULAR:
                return getResources().getString(R.string.popular);
            case RATINGS:
                return getResources().getString(R.string.top_rated);
            case FAVORITE:
                return getResources().getString(R.string.favorite);
            default:
                return getResources().getString(R.string.popular);
        }
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

}
