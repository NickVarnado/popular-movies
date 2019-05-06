package com.nbvarnado.popularmovies.ui.main;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nbvarnado.popularmovies.R;
import com.nbvarnado.popularmovies.data.Preferences;
import com.nbvarnado.popularmovies.ui.detail.MovieDetailsActivity;
import com.nbvarnado.popularmovies.util.InjectorUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements MovieAdapter.MovieClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private final String TAG = MainActivity.class.getSimpleName();

    // Intent Extra
    public static final String EXTRA_MOVIE_ID = "extra_movie_id";

    @BindView(R.id.rv_movies) RecyclerView mRecyclerView;
    @BindView(R.id.pb_loading) ProgressBar mLoadingIndicator;
    @BindView(R.id.tv_error_message) TextView mErrorMessage;

    private MovieAdapter mMovieAdapter;
    private MainActivityViewModel mMainActivityViewModel;
    private String sort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        sort = Preferences.getSortingPreference(this, getString(R.string.sort_key));

        MainViewModelFactory factory = InjectorUtils.provideMainViewModelFactory(this, sort);
        mMainActivityViewModel = ViewModelProviders.of (this, factory).get(MainActivityViewModel.class);
        mMainActivityViewModel.getMovies().observe(this, movies -> {
            if (movies != null) {
                mMovieAdapter.setMovieData(movies);
                mMovieAdapter.notifyDataSetChanged();
                showMovies();
            } else {
                showError();
            }
        });

        mRecyclerView.setHasFixedSize(true);

        int spanCount = calculateNumberOfColumns(getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,
                spanCount,
                RecyclerView.VERTICAL,
                false);
        
        mRecyclerView.setLayoutManager(layoutManager);
        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        showLoading();
    }

    @Override
    public void onMovieClick(int id) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra(EXTRA_MOVIE_ID, id);
        startActivity(intent);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        int selectedId;
        switch (sort) {
            case "popular":
                selectedId = R.id.sort_popular;
                break;
            case "top_rated":
                selectedId = R.id.sort_rating;
                break;
            case "favorite":
                selectedId = R.id.sort_favorite;
                break;
            default:
                selectedId = R.id.sort_popular;
        }
        MenuItem selectedMenuItem = menu.findItem(selectedId);
        selectedMenuItem.setChecked(true);
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
                sort = getResources().getString(R.string.popular);
                Preferences.setSortingPreference(this, sort);
                mMainActivityViewModel.setmSort(sort);
                showLoading();
                return true;
            case R.id.sort_rating:
                sort = getResources().getString(R.string.top_rated);
                Preferences.setSortingPreference(this, sort);
                mMainActivityViewModel.setmSort(sort);
                showLoading();
                return true;
            case R.id.sort_favorite:
                sort = getResources().getString(R.string.favorite);
                Preferences.setSortingPreference(this, sort);
                mMainActivityViewModel.setmSort(sort);
                showLoading();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        sort = Preferences.getSortingPreference(this, key);
        mMovieAdapter.setMovieData(null);
        mMainActivityViewModel.setmSort(getResources().getString(R.string.popular));
    }

    public static int calculateNumberOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 200;
        int numberOfColumns = (int) (dpWidth / scalingFactor);
        if (numberOfColumns < 2) {
            return 2;
        }
        return numberOfColumns;
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
