package com.nbvarnado.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Intent intent = getIntent();

        Bundle bundle = intent.getBundleExtra(MainActivity.EXTRA_MOVIE);
        String title = bundle.getString(MainActivity.TITLE);
        String imageUrl = bundle.getString(MainActivity.IMAGE);
        String releaseDate = bundle.getString(MainActivity.RELEASE_DATE);
        String voteAverage = bundle.getString(MainActivity.VOTE_AVERAGE);
        String overview = bundle.getString(MainActivity.OVERVIEW);

        TextView movieTitleTextView = findViewById(R.id.tv_detail_movie_title);
        movieTitleTextView.setText(title);

        ImageView moviePosterImageView = findViewById(R.id.iv_detail_movie_poster);
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.user_placeholder)
                .error(R.drawable.user_placeholder_error)
                .into(moviePosterImageView);

        TextView releaseDateTextView = findViewById(R.id.tv_release_date);
        releaseDateTextView.setText(getYearString(releaseDate));

        TextView ratingTextView = findViewById(R.id.tv_rating);
        ratingTextView.setText(formatVoteAverage(voteAverage));

        TextView overviewTextView = findViewById(R.id.tv_overview);
        overviewTextView.setText(overview);

    }

    /**
     * Takes a date String in this format YYYY-MM-DD and returns the year String.
     * @param date String in the following format: YYYY-MM-DD
     * @return String representation of the movies year.
     */
    private String getYearString(String date) {
        final Character DELIMITER = '-';
        int index = date.indexOf(DELIMITER);
        return date.substring(0, index);
    }

    /**
     * Formats the movies vote average to be out of 10.
     * @param voteAverage of the movie.
     * @return String representation of the movies vote average out of 10.
     */
    private String formatVoteAverage(String voteAverage) {
        return voteAverage + "/10";
    }
}
