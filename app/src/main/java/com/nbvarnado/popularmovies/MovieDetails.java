package com.nbvarnado.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetails extends AppCompatActivity {

    private static final String TAG = MovieDetails.class.getSimpleName();

    private static final String OLD_DATE_FORMAT = "yyyy-MM-dd";
    private static final String NEW_DATE_FORMAT = "MMM d, yyyy";
    private static final String MAX_VOTE = "/10";

    @BindView(R.id.tv_detail_movie_title) TextView movieTitleTextView;
    @BindView(R.id.iv_detail_movie_poster) ImageView moviePosterImageView;
    @BindView(R.id.tv_release_date) TextView releaseDateTextView;
    @BindView(R.id.tv_rating) TextView ratingTextView;
    @BindView(R.id.tv_overview) TextView overviewTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(MainActivity.EXTRA_MOVIE);

        String title = bundle.getString(MainActivity.TITLE);
        String imageUrl = bundle.getString(MainActivity.IMAGE);
        String releaseDate = bundle.getString(MainActivity.RELEASE_DATE);
        String voteAverage = bundle.getString(MainActivity.VOTE_AVERAGE);
        String overview = bundle.getString(MainActivity.OVERVIEW);

        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.user_placeholder)
                .error(R.drawable.user_placeholder_error)
                .into(moviePosterImageView);

        movieTitleTextView.setText(title);
        releaseDateTextView.setText(getFormattedDate(releaseDate));
        ratingTextView.setText(formatVoteAverage(voteAverage));
        overviewTextView.setText(overview);

    }

    /**
     * Takes a date String in this format YYYY-MM-DD and returns the year String.
     * @param dateString String in the following format: YYYY-MM-DD
     * @return String representation of the movies year.
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
     * Formats the movies vote average to be out of 10.
     * @param voteAverage of the movie.
     * @return String representation of the movies vote average out of 10.
     */
    private String formatVoteAverage(String voteAverage) {
        return voteAverage + MAX_VOTE;
    }
}
