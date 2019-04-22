package com.nbvarnado.popularmovies.data.database.trailers;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface TrailerDao {

    @Query("SELECT * FROM trailer_table WHERE movieId=:id")
    LiveData<List<Trailer>> getTrailers(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTrailers(List<Trailer> trailers);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTrailer(Trailer trailer);
}
