package com.nbvarnado.popularmovies.data.database.movies;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie_table WHERE isFavorite=1")
    List<Movie> getFavoriteMovies();

    @Query("SELECT * FROM movie_table WHERE sortBy=:sort")
    List<Movie> getMovies(String sort);

    @Query("SELECT * FROM movie_table WHERE id = :movieId")
    LiveData<Movie> getMovieById(int movieId);

    @Query("UPDATE movie_table SET isFavorite = :isFavorite WHERE id = :movieId")
    void toggleFavorite(int isFavorite, int movieId);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(Movie movies);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    public void insertMovies(Movie... movies);
}
