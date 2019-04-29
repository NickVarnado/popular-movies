package com.nbvarnado.popularmovies.ui.main;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nbvarnado.popularmovies.R;
import com.nbvarnado.popularmovies.data.database.movies.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Nick Varnado on 2/19/2019.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> mMovies;

    final private MovieClickListener mOnClickListener;
    /**
     * The interface for onClick.
     */
    public interface MovieClickListener {
        void onMovieClick(int id);
    }

    MovieAdapter(MovieClickListener listener) {
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View moviePosterView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_poster, viewGroup, false);
        return new MovieViewHolder(moviePosterView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int i) {
        Movie movie = mMovies.get(i);
        String imageUrl = getImageUrl(movie.getPosterPath());
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.user_placeholder)
                .error(R.drawable.error)
                .into(movieViewHolder.mImageView);
    }

    @Override
    public int getItemCount() {
        if (mMovies == null) {
            return 0;
        }
        return mMovies.size();
    }

    /**
     * Sets the movie data.
     * @param movieData List of Movie.
     */
    void setMovieData(List<Movie> movieData) {
        mMovies = movieData;
    }

    /**
     * Get's the image URL String.
     * @param posterPath path for the poster
     * @return The String for the image URL.
     */
    private String getImageUrl(String posterPath) {
        String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
        // Size can be one of the following. "w92", "w154", "w185", "w342", "w500", "w780", or "original".
        String IMAGE_SIZE = "w185";
        return IMAGE_BASE_URL + IMAGE_SIZE + posterPath;
    }

    /**
     * ViewHolder for the MovieAdapter.
     */
    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView mImageView;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.iv_movie_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Movie movie = mMovies.get(position);
            int id = movie.getId();
            mOnClickListener.onMovieClick(id);
        }
    }

}
