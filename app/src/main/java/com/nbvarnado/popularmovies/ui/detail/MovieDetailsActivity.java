package com.nbvarnado.popularmovies.ui.detail;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nbvarnado.popularmovies.AppExecutors;
import com.nbvarnado.popularmovies.data.database.movies.Movie;
import com.nbvarnado.popularmovies.ui.detail.review.ReviewAdapter;
import com.nbvarnado.popularmovies.ui.detail.trailer.TrailerAdapter;
import com.nbvarnado.popularmovies.ui.main.MainActivity;
import com.nbvarnado.popularmovies.R;
import com.nbvarnado.popularmovies.util.InjectorUtils;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailsActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

    private static final String OLD_DATE_FORMAT = "yyyy-MM-dd";
    private static final String NEW_DATE_FORMAT = "MMM d, yyyy";
    private static final String MAX_VOTE = "/10";

    private static MovieDetailsViewModel mMovieDetailsViewModel;
    private static ReviewAdapter mReviewAdapter;
    private static TrailerAdapter mTrailerAdapter;

    private Movie mMovie;

    @BindView(R.id.tv_detail_movie_title) TextView movieTitleTextView;
    @BindView(R.id.iv_detail_movie_poster) ImageView moviePosterImageView;
    @BindView(R.id.tv_release_date) TextView releaseDateTextView;
    @BindView(R.id.tv_rating) TextView ratingTextView;
    @BindView(R.id.tv_overview) TextView overviewTextView;
    @BindView(R.id.ib_favoriteMovies) ImageButton favoriteMoviesImageButton;
    @BindView(R.id.rv_trailers) RecyclerView mTrailersRecyclerView;
    @BindView(R.id.rv_reviews) RecyclerView mReviewsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        mTrailerAdapter = new TrailerAdapter(this);
        mReviewAdapter = new ReviewAdapter();
        mTrailersRecyclerView.setHasFixedSize(true);
        mReviewsRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager trailerLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        RecyclerView.LayoutManager reviewLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mTrailersRecyclerView.setLayoutManager(trailerLayoutManager);
        mReviewsRecyclerView.setLayoutManager(reviewLayoutManager);
        mTrailersRecyclerView.setAdapter(mTrailerAdapter);
        mReviewsRecyclerView.setAdapter(mReviewAdapter);

        Intent intent = getIntent();
        int id = intent.getIntExtra(MainActivity.EXTRA_MOVIE_ID, 0);

        MovieDetailsViewModelFactory factory = InjectorUtils.provideMovieDetailsViewModelFactory(getApplicationContext(), id);
        mMovieDetailsViewModel = ViewModelProviders.of(this, factory).get(MovieDetailsViewModel.class);

        mMovieDetailsViewModel.getMovie().observe(this, this::populateDetailsUI);

        mMovieDetailsViewModel.getReviews().observe(this, reviews -> {
            if (reviews != null && reviews.size() > 0) {
                mReviewAdapter.setReviewData(reviews);
                mReviewAdapter.notifyDataSetChanged();
            }
        });

        mMovieDetailsViewModel.getTrailers().observe(this, trailers -> {
            if (trailers != null && trailers.size() > 0) {
                mTrailerAdapter.setTrailerData(trailers);
                mTrailerAdapter.notifyDataSetChanged();
            }
        });

        favoriteMoviesImageButton.setOnClickListener(v -> {
            final int isFav = (mMovie.getIsFavorite() == 0) ? 1 : 0;
            AppExecutors.getInstance().diskIO().execute(() -> {
                InjectorUtils.provideRepository(getApplicationContext()).toggleFavorite(isFav, mMovie.getId());
            });
            StringBuilder sb = new StringBuilder();
            sb.append(mMovie.getTitle());
            if (isFav == 1) {
                sb.append(" added to favorites.");
            } else {
                sb.append(" removed from favorites.");
            }
            Toast favoriteToggleToast = Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_SHORT);
            favoriteToggleToast.show();
        });

    }

    private void populateDetailsUI(Movie movie) {
        mMovie = movie;
        String imageUrl = getImgageUrl(movie.getPosterPath());

        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.user_placeholder)
                .error(R.drawable.error)
                .into(moviePosterImageView);

        movieTitleTextView.setText(movie.getTitle());
        releaseDateTextView.setText(getFormattedDate(movie.getReleaseDate()));
        ratingTextView.setText(formatVoteAverage(String.valueOf(movie.getVoteAverage())));
        overviewTextView.setText(movie.getOverview());

        int bkgd = (movie.getIsFavorite() == 0) ?
                android.R.drawable.star_big_off : android.R.drawable.star_big_on;

        favoriteMoviesImageButton.setBackgroundResource(bkgd);

    }

    /**
     * Takes a date String in this format YYYY-MM-DD and returns a date String in the following
     * format MMM d, yyyy.
     * @param dateString String in the following format: YYYY-MM-DD
     * @return String representation of the movies release date in the format MMM d, yyyy.
     */
    private String getFormattedDate(String dateString) {
        SimpleDateFormat oldDateFormat = new SimpleDateFormat(OLD_DATE_FORMAT, Locale.US);
        SimpleDateFormat newDateFormat = new SimpleDateFormat(NEW_DATE_FORMAT, Locale.US);
        try {
            Date date = oldDateFormat.parse(dateString);
            return newDateFormat.format(date);
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
            return "";
        }
    }

    /**
     * Get's the image URL String.
     * @param posterPath path for the poster
     * @return The String for the image URL.
     */
    public String getImgageUrl(String posterPath) {
        String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
        // Size can be one of the following. "w92", "w154", "w185", "w342", "w500", "w780", or "original".
        String IMAGE_SIZE = "w185";
        return IMAGE_BASE_URL + IMAGE_SIZE + posterPath;
    }

    /**
     * Formats the movies vote average to be out of 10.
     * @param voteAverage of the movie.
     * @return String representation of the movies vote average out of 10.
     */
    private String formatVoteAverage(String voteAverage) {
        return voteAverage + MAX_VOTE;
    }
}
