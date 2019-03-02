package com.nbvarnado.popularmovies;

/**
 * Created by Nick Varnado on 3/1/2019.
 */

import com.nbvarnado.popularmovies.model.Page;

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
    Call<Page> getResults(@Path("sort") String sort, @Query(API_KEY_PARAM) String apiKey);
}
