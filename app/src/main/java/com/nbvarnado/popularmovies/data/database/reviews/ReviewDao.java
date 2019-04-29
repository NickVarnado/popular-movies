package com.nbvarnado.popularmovies.data.database.reviews;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ReviewDao {

    @Query("SELECT * FROM review_table where movieId=:id")
    List<Review> getReviews(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertReviews(Review... reviews);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertReview(Review review);

}
