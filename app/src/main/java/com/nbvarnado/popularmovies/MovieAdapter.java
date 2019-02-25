package com.nbvarnado.popularmovies;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nbvarnado.popularmovies.model.Movie;
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
        void onMovieClick(Movie movie);
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
        Picasso.get()
                .load(movie.getImageUrl())
                .placeholder(R.drawable.user_placeholder)
                .error(R.drawable.user_placeholder_error)
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
     * @param movieData List of Movies.
     */
    public void setMovieData(List<Movie> movieData) {
        mMovies = movieData;
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
            mOnClickListener.onMovieClick(movie);
        }
    }
}
