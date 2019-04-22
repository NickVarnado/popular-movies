package com.nbvarnado.popularmovies.data.network;

/**
 * Created by Nick Varnado on 3/1/2019.
 */

import com.nbvarnado.popularmovies.data.database.movies.MoviesPage;
import com.nbvarnado.popularmovies.data.database.reviews.ReviewsPage;
import com.nbvarnado.popularmovies.data.database.trailers.TrailersPage;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface for querying themoviedb API.
 */
public interface MovieDbService {

    String API_KEY_PARAM = "api_key";

    @GET("{sort}/")
    Call<MoviesPage> getMovies(@Path("sort") String sort, @Query(API_KEY_PARAM) String apiKey);

    @GET("{movie_id}/reviews")
    Call<ReviewsPage> getReviews(@Path("movie_id") String movieId, @Query(API_KEY_PARAM) String apiKey);

    @GET("{movie_id}/videos")
    Call<TrailersPage> getTrailers(@Path("movie_id") String movieId, @Query(API_KEY_PARAM) String apiKey);
}
