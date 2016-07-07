package com.minutes.moviesdatabase.api;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by barikos on 13.06.16.
 */
public interface MovieDBService {

    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String key, @Query("page") int page);

    @GET("movie/{id}")
    Call<MovieResponse> getMovieDetails(@Path("id") int id, @Query("api_key") String key);

    @GET("/3/discover/movie")
    Call<MovieResponse> getMovies(@Query("api_key") String key,
                            @Query("page") String page,
                            @Query("sort_by") String filter);
}
